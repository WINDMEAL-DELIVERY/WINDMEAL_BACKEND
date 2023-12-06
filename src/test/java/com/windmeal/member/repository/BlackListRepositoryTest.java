package com.windmeal.member.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.windmeal.IntegrationTestSupport;
import com.windmeal.member.domain.BlackList;
import com.windmeal.member.domain.Member;
import com.windmeal.member.dto.response.BlackListResponse;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.windmeal.Fixtures.aMember;
class BlackListRepositoryTest extends IntegrationTestSupport {

  @Autowired
  BlackListRepository blackListRepository;

  @Autowired
  MemberRepository memberRepository;

  @DisplayName("")
  @Test
  void getBlackListByRequesterId() {
    //given
    Member requester = memberRepository.save(aMember().build());
    Member blacked = memberRepository.save(aMember().email("test").build());
    blackListRepository.save(BlackList.place(requester,blacked));

    //when
    Page<BlackListResponse> blackListByRequesterId = blackListRepository.getBlackListByRequesterId(
        Pageable.unpaged(), requester.getId());

    //then
    Assertions.assertThat(blackListByRequesterId).hasSize(1)
        .extracting("email")
        .containsExactly("test");
  }


  @DisplayName("")
  @Test
  void getBlackListByBlackMemberAndRequesterIn(){
      //given

      //when
          blackListRepository.getBlackListByBlackMemberAndRequesterIn(0L, Arrays.asList(1L,2L));
      //then
  }
}