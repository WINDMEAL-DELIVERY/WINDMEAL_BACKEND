package com.windmeal.member.service;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.windmeal.annotation.WithMockCustomUser;
import com.windmeal.member.controller.MemberController;
import com.windmeal.member.dto.request.NicknameRequest;
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

//@ControllerTest(MemberController.class)
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

  @Test
  @WithMockCustomUser
  @DisplayName("닉네임 등록 태스트 - 성공")
  public void registerNicknameSuccess() throws Exception {
    //given
    NicknameRequest nicknameRequest = createNicknameRequest("뚜벅이");

    //when
    when(memberService.registerNickname(nicknameRequest, 1L))
        .thenReturn("뚜벅이");

    //then
    mockMvc.perform(
            post("/api/member/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(nicknameRequest)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.success").value("true"));
  }

}
