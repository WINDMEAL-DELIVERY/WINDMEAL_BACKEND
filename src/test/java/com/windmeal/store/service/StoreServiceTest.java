package com.windmeal.store.service;

import com.windmeal.member.domain.Member;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.response.StoreResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("매장을 생성한다.")
    @Test
    void createStore(){
        //given
        Member savedMember = createMember("test");
        StoreCreateRequest request = createStoreCreateRequest(
                savedMember,"010","testName"
                ,LocalTime.of(10, 0, 0),LocalTime.of(22, 0, 0)
                ,1234.567, 567.812);

        //when
        StoreResponse response = storeService.createStore(request,"photo");

        //then
        assertThat(response)
                .extracting("ownerId","name","phoneNumber","photo","openTime","closeTime","location")
                .containsExactly(savedMember.getId(),"testName","010","photo",LocalTime.of(10,0,0),LocalTime.of(22, 0, 0),new Point(1234.567, 567.812));
    }

    private static StoreCreateRequest createStoreCreateRequest(
            Member savedMember,String phoneNumber,String name, LocalTime openTime, LocalTime closeTime, Double lat, Double lon) {
        return StoreCreateRequest.builder()
                .memberId(savedMember.getId())
                .name(name)
                .phoneNumber(phoneNumber)
                .memberId(savedMember.getId())
                .openTime(openTime)
                .closeTime(closeTime)
                .latitude(lat)
                .longitude(lon)
                .build();
    }

    private Member createMember(String name) {
        return memberRepository.save(Member.builder()
                .name(name).build());
    }



}