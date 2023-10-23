package com.windmeal.global.security.oauth2.impl;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.security.exception.InvalidEmailDomainException;
import com.windmeal.global.security.oauth2.provider.OAuth2UserInfoFactory;
import com.windmeal.global.security.oauth2.user.OAuth2UserInfo;
import com.windmeal.global.security.oauth2.user.UserPrincipal;
import com.windmeal.member.domain.Authority;
import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    @Value("${oauth2.provider.google.token-revoke-url}")
    private String revokeUrl;

    /*
    인증이 성공했을 때 호출될 메서드
    여기서 회원 여부를 확인하고, 회원이 아니라면 가입까지 진행시킨다.
    추가로 학교 이메일로 가입한 것인지도 확인한다.
     */

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String providerName = oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase();
        String accessTokenValue = oAuth2UserRequest.getAccessToken().getTokenValue();
        validateAttributes(attributes, accessTokenValue);
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerName, attributes);
        Member member = registerIfNewUser(oAuth2UserInfo);
        return UserPrincipal.create(member, oAuth2UserInfo.getAttributes());
    }

    /*
        현재 클래스까지 왔다는 것은 이미 provider로부터 토큰까지 모두 전달받았다는 것이다.
        따라서 도메인이 다르거나, 우리가 원하는 정보가 없을 경우에는 다시 발행된 토큰을 취소해줘야 한다.
        토큰 취소를 위해서 매개변수로 token을 가진다.
     */
    private void validateAttributes(Map<String, Object> userInfoAttributes, String token) {
        if(!userInfoAttributes.containsKey("email")) {
            throw new IllegalArgumentException("응답에 email이 존재하지 않습니다.");
        }
        String email = (String)userInfoAttributes.get("email");
        String[] tokens = email.split("@");
        if(!tokens[1].equals("gachon.ac.kr")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            ResponseEntity<String> response = restTemplate.postForEntity(revokeUrl + "?token=" + token, headers, String.class);
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("토큰이 취소되었습니다.");
            } else {
                log.info("토큰이 취소에 실패하였습니다.");
            }
            throw new InvalidEmailDomainException(ErrorCode.VALIDATION_ERROR, "가천대학교 계정이 아닙니다. 토큰을 취소합니다.");
        }
    }

    @Transactional
    private Member registerIfNewUser(OAuth2UserInfo oAuth2UserInfo) {
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuth2UserInfo.getEmail());
        Member member;
        if(!optionalMember.isPresent()) {
            member = memberRepository.save(createMember(oAuth2UserInfo));
        } else {
            member = optionalMember.get();
        }
        return member;
    }

    private Member createMember(OAuth2UserInfo oAuth2UserInfo) {
        String[] personalInfos = oAuth2UserInfo.getName().split("/");
        Member member = Member.builder()
                .name(personalInfos[0])
                .email(oAuth2UserInfo.getEmail())
                .department(personalInfos[1])
                .authority(Authority.ROLE_USER)
                .build();
        // TODO 닉네임 입력을 받아야 하는데, 가입이 완료된 후 별도로 다시 받아서 설정해줘야 할 것 같다.
        return member;
    }
}
