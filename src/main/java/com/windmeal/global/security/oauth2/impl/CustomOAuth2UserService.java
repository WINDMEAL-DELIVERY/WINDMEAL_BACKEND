package com.windmeal.global.security.oauth2.impl;

import com.windmeal.global.security.exception.InvalidEmailDomainException;
import com.windmeal.global.security.oauth2.user.UserPrincipal;
import com.windmeal.member.domain.Authority;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import java.util.*;

import static com.windmeal.global.constants.S3Constant.WINDMEAL_DEFAULT_PROFILE_URL;
import static com.windmeal.global.constants.SecurityConstants.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final MemberRepository memberRepository;
  private final RestTemplate restTemplate;
  @Value("${oauth2.provider.google.token-revoke-url}")
  private String revokeUrl;
  private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
  private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {
  };


  /**
   * 인증이 성공했을 경우 호출되며, 사용자를 회원가입 시킨다.<br></br> 이미 회원가입한 사용자에 한해 다른 경로로 리다이렉션 시키기 위해 <br></br>
   * {@link DefaultOAuth2UserService} 의 loadUser를 커스텀하였다. <br></br> 데이터베이스에서 사용자를 찾고, attributes를
   * 얻어서 nickname이 존재한다면 Map에 추가해준다. <br></br> 이후에 호출되는
   * {@link com.windmeal.global.security.handler.CustomSuccessHandler} 에서 <br></br> 닉네임의 존재 여부를 알 수
   * 있고, 그에 따라 리다이렉션 로직을 분기할 수 있다.
   *
   * @param userRequest the user request
   * @return {@link OAuth2User}
   * @throws OAuth2AuthenticationException
   */
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    log.info("CustomOAuth2UserService, loadUser : ");
    Map<String, Object> attributes = superClassLoadUser(userRequest);
    String accessTokenValue = userRequest.getAccessToken().getTokenValue();
    processDomain(attributes, accessTokenValue);
    Member member = registerIfNewUser(attributes);

    if (StringUtils.hasText(member.getNickname())) {
      attributes.put("nickname", member.getNickname());
    }
    Set<GrantedAuthority> authorities = new LinkedHashSet<>();
    authorities.add(new OAuth2UserAuthority(attributes));
    OAuth2AccessToken token = userRequest.getAccessToken();
    for (String authority : token.getScopes()) {
      authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
    }
    return UserPrincipal.create(member, attributes);
  }


  /**
   * 사용자의 도메인이 유효하지 않을 경우 토큰을 취소하고 에러 페이지로 리다이렉트 시킨다. (예외 발생)
   * @param userInfoAttributes
   * @param token
   * @throws OAuth2AuthenticationException
   */
  private void processDomain(Map<String, Object> userInfoAttributes, String token)
      throws OAuth2AuthenticationException {
    if (!userInfoAttributes.containsKey("email")) {
      throw new IllegalArgumentException("응답에 email이 존재하지 않습니다.");
    }
    if (!userInfoAttributes.getOrDefault("hd", "").equals("gachon.ac.kr")) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      ResponseEntity<String> response = restTemplate.postForEntity(revokeUrl + "?token=" + token,
          headers, String.class);
      if (response.getStatusCode().is2xxSuccessful()) {
        log.info("토큰이 취소되었습니다.");
      } else {
        log.info("토큰이 취소에 실패하였습니다.");
      }
      throw new InvalidEmailDomainException(
          new OAuth2Error("401", "가천대학교 계정이 아닙니다. 토큰을 취소합니다.", ERROR_REDIRECT)
      );
    }
  }

  @Transactional
  private Member registerIfNewUser(Map<String, Object> attributes) {
    Optional<Member> optionalMember = memberRepository.findByEmail(
        (String) attributes.get("email"));
    Member member;
    member = optionalMember.orElseGet(() -> memberRepository.save(createMember(attributes)));
    return member;
  }

  private Member createMember(Map<String, Object> attributes) {
    int randNum = new Random().nextInt(10) + 1;
    Member member = Member.builder()
        .name((String) attributes.get("family_name"))
        .email((String) attributes.get("email"))
        .department((String) attributes.get("given_name"))
        .profileImage(WINDMEAL_DEFAULT_PROFILE_URL + randNum + ".png")
        .authority(Authority.ROLE_USER)
        .build();
    return member;
  }


  /**
   * {@link OAuth2UserService} 인터페이스를 구현하는
   * {@link DefaultOAuth2UserService} 에서 가져온 클래스들이다.
   * <br></br>
   * 내부 로직의 처리 과정에서 닉네임을 고려하기 위해 기존 로직을 커스터마이징하였으며, 원본의 정보는 아래와 같음.
   *
   * @author Joe Grandja
   * @see OAuth2UserService
   * @see OAuth2UserRequest
   * @see OAuth2User
   * @see DefaultOAuth2User
   * @since 5.0
   */
  private Map<String, Object> superClassLoadUser(OAuth2UserRequest userRequest)
      throws OAuth2AuthenticationException {
    Assert.notNull(userRequest, "userRequest cannot be null");
    if (!StringUtils
        .hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
            .getUri())) {
      OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_INFO_URI_ERROR_CODE,
          "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: "
              + userRequest.getClientRegistration().getRegistrationId(),
          null);
      throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
    }
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint()
        .getUserNameAttributeName();
    if (!StringUtils.hasText(userNameAttributeName)) {
      OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE,
          "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: "
              + userRequest.getClientRegistration().getRegistrationId(),
          null);
      throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
    }
    RequestEntity<?> request = requestEntityConverter.convert(userRequest);
    ResponseEntity<Map<String, Object>> response = superClassGetResponse(userRequest, request);
    Map<String, Object> userAttributes = response.getBody();

    return userAttributes;
  }


  private ResponseEntity<Map<String, Object>> superClassGetResponse(OAuth2UserRequest userRequest,
      RequestEntity<?> request) {
    try {
      return restTemplate.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
    } catch (OAuth2AuthorizationException ex) {
      OAuth2Error oauth2Error = ex.getError();
      StringBuilder errorDetails = new StringBuilder();
      errorDetails.append("Error details: [");
      errorDetails.append("UserInfo Uri: ")
          .append(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
              .getUri());
      errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
      if (oauth2Error.getDescription() != null) {
        errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
      }
      errorDetails.append("]");
      oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
          "An error occurred while attempting to retrieve the UserInfo Resource: "
              + errorDetails.toString(),
          null);
      throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
    } catch (UnknownContentTypeException ex) {
      String errorMessage =
          "An error occurred while attempting to retrieve the UserInfo Resource from '"
              + userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
              .getUri()
              + "': response contains invalid content type '" + ex.getContentType().toString()
              + "'. "
              + "The UserInfo Response should return a JSON object (content type 'application/json') "
              + "that contains a collection of name and value pairs of the claims about the authenticated End-User. "
              + "Please ensure the UserInfo Uri in UserInfoEndpoint for Client Registration '"
              + userRequest.getClientRegistration().getRegistrationId()
              + "' conforms to the UserInfo Endpoint, "
              + "as defined in OpenID Connect 1.0: 'https://openid.net/specs/openid-connect-core-1_0.html#UserInfo'";
      OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE, errorMessage,
          null);
      throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
    } catch (RestClientException ex) {
      OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
          "An error occurred while attempting to retrieve the UserInfo Resource: "
              + ex.getMessage(), null);
      throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
    }
  }

}

