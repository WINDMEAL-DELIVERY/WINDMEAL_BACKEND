package com.windmeal.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.annotation.WithMockCustomUser;
import com.windmeal.global.exception.AesException;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.controller.MemberController;
import com.windmeal.member.dto.request.MemberInfoRequest;
import com.windmeal.member.dto.request.NicknameRequest;
import com.windmeal.member.dto.response.MemberInfoDTO;
import com.windmeal.member.exception.DuplicatedNicknameException;
import com.windmeal.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class MemberControllerTest {

  @MockBean
  MemberService memberService;

  @MockBean
  MappingMongoConverter mappingMongoConverter;

  @Autowired
  ObjectMapper objectMapper;

  private static MockMvc mockMvc;

  @BeforeAll
  public static void setup(WebApplicationContext webApplicationContext) {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .addFilters(new CharacterEncodingFilter("UTF-8", true))
        .apply(sharedHttpSession())
        .build();
  }

  private NicknameRequest createNicknameRequest(String nickname) {
    return NicknameRequest.of(nickname);
  }

  private MemberInfoRequest createMemberInfoRequest(String alarmToken) {
    return MemberInfoRequest.of(alarmToken);
  }

  private MemberInfoDTO createMemberInfoDTO(Long memberId) {
    return MemberInfoDTO.of(memberId, "temp@gachon.ac.kr", "Owen");
  }

  @Test
  @WithMockCustomUser
  @DisplayName("닉네임 등록 태스트 - 성공")
  public void registerNicknameSuccess() throws Exception {
    //given
    String nickname = "뚜벅이";
    NicknameRequest nicknameRequest = createNicknameRequest(nickname);
    //when
    when(memberService.registerNickname(nicknameRequest, 1L))
        .thenReturn(nickname);

    //then
    mockMvc.perform(
            post("/api/member/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(nicknameRequest)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.success").value("true"));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("닉네임 등록 태스트 - 실패 : 이미 사용 중인 닉네임")
  public void registerDuplicatedNickname() throws Exception {
    //given
    NicknameRequest nicknameRequest = createNicknameRequest("뚜벅이");

    //when
    when(memberService.registerNickname(any(), any()))
        .thenThrow(new DuplicatedNicknameException());

    //then
    mockMvc.perform(
            post("/api/member/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(nicknameRequest)))
        .andDo(print())
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
        .andExpect(jsonPath("$.message").value("이미 사용 중인 닉네임입니다."));
  }

  @Test
  @DisplayName("닉네임 등록 태스트 - 실패 : 인증되지 않은 사용자")
  public void registerNicknameOfUnauthorizedMember() throws Exception {
    //given
    NicknameRequest nicknameRequest = createNicknameRequest("뚜벅이");

    //when & then
    mockMvc.perform(
            post("/api/member/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(nicknameRequest)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()))
        .andExpect(jsonPath("$.message").value("인증되지 않은 사용자입니다."));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("닉네임 등록 태스트 - 실패 : 존재하지 않는 사용자")
  public void registerNicknameOfEmptyMember() throws Exception {
    //given
    NicknameRequest nicknameRequest = createNicknameRequest("뚜벅이");

    //when
    when(memberService.registerNickname(any(), any()))
        .thenThrow(new MemberNotFoundException());

    //then
    mockMvc.perform(
            post("/api/member/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(nicknameRequest)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND.getCode()))
        .andExpect(jsonPath("$.message").value("존재하지 않는 사용자입니다."));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("닉네임 등록 태스트 - 실패 : 닉네임 제약조건 위반 - 특수문자")
  public void registerInvalidNickname() throws Exception {
    //given
    NicknameRequest nicknameRequest = createNicknameRequest("#$%#$@$_특수문자_// 제약조건");

    // when & then
    mockMvc.perform(
            post("/api/member/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(nicknameRequest)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
        .andExpect(jsonPath("$.message").value("닉네임에는 특수문자가 포함될 수 없습니다."));
  }


  @Test
  @WithMockCustomUser
  @DisplayName("닉네임 등록 태스트 - 실패 : 닉네임 제약조건 위반 - 빈칸")
  public void registerEmptyNickname() throws Exception {
    //given
    NicknameRequest nicknameRequest = createNicknameRequest("");

    // when & then
    mockMvc.perform(
            post("/api/member/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(nicknameRequest)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
        .andExpect(jsonPath("$.message").value("닉네임은 빈칸이 될 수 없습니다."));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("알람 토큰과 사용자 정보 교환 테스트 - 성공")
  public void tokenExchangeSuccess() throws Exception {
    //given
    String alarmToken = "alarmToken";
    MemberInfoRequest memberInfoRequest = createMemberInfoRequest(alarmToken);

    //when
    when((memberService.memberInfoDetails(memberInfoRequest, 1L)))
        .thenReturn(createMemberInfoDTO(1L));

    //then
    mockMvc.perform(
            post("/api/member/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(memberInfoRequest)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.success").value("true"));
  }

  @Test
  @DisplayName("알람 토큰과 사용자 정보 교환 테스트 - 실패 : 인증되지 않은 사용자")
  public void tokenExchangeWithUnauthorizedMember() throws Exception {
    //given
    String alarmToken = "alarmToken";
    MemberInfoRequest memberInfoRequest = createMemberInfoRequest(alarmToken);

    //then
    mockMvc.perform(
            post("/api/member/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(memberInfoRequest)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()))
        .andExpect(jsonPath("$.message").value("인증되지 않은 사용자입니다."));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("알람 토큰과 사용자 정보 교환 테스트 - 실패 : 존재하지 않는 사용자")
  public void tokenExchangeWithEmptyMember() throws Exception {
    //given
    String alarmToken = "alarmToken";
    MemberInfoRequest memberInfoRequest = createMemberInfoRequest(alarmToken);

    // when
    when(memberService.memberInfoDetails(any(), any()))
        .thenThrow(new MemberNotFoundException());

    //then
    mockMvc.perform(
            post("/api/member/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(memberInfoRequest)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND.getCode()))
        .andExpect(jsonPath("$.message").value("존재하지 않는 사용자입니다."));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("알람 토큰과 사용자 정보 교환 테스트 - 실패 : 암호화 실패")
  public void tokenExchangeOfInvalidEncryption() throws Exception {
    //given
    String alarmToken = "alarmToken";
    MemberInfoRequest memberInfoRequest = createMemberInfoRequest(alarmToken);

    // when
    when(memberService.memberInfoDetails(any(), any()))
        .thenThrow(new AesException(ErrorCode.ENCRYPT_ERROR));

    //then
    mockMvc.perform(
            post("/api/member/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(memberInfoRequest)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.code").value(ErrorCode.ENCRYPT_ERROR.getCode()))
        .andExpect(jsonPath("$.message").value("암호화 과정에서 에러가 발생하였습니다."));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("알람 토큰과 사용자 정보 교환 테스트 - 실패 : 토큰 제약조건 위반 - 빈칸")
  public void tokenExchangeOfInvalidToken() throws Exception {
    //given
    MemberInfoRequest memberInfoRequest = createMemberInfoRequest("");

    // when & then
    mockMvc.perform(
            post("/api/member/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(memberInfoRequest)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
        .andExpect(jsonPath("$.message").value("토큰값은 반드시 존재해야 합니다."));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("사용자 정보 반환 테스트 - 성공")
  public void memberInfoSuccess() throws Exception {
    //given & when
    when(memberService.myInfoDetails(1L))
        .thenReturn(createMemberInfoDTO(1L));

    //then
    mockMvc.perform(
            get("/api/member")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.success").value("true"));
  }

  @Test
  @DisplayName("사용자 정보 반환 테스트 - 실패 : 인증되지 않은 사용자")
  public void unauthorizedMemberInfo() throws Exception {
    //given & when & then
    mockMvc.perform(
            get("/api/member")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()))
        .andExpect(jsonPath("$.message").value("인증되지 않은 사용자입니다."));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("사용자 정보 반환 테스트 - 실패 : 존재하지 않는 사용자")
  public void emptyMemberInfo() throws Exception {
    //given & when
    when(memberService.myInfoDetails(1L))
        .thenThrow(new MemberNotFoundException());

    //then
    mockMvc.perform(
            get("/api/member")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND.getCode()))
        .andExpect(jsonPath("$.message").value("존재하지 않는 사용자입니다."));
  }

}