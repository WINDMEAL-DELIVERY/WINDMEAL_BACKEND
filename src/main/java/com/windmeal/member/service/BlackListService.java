package com.windmeal.member.service;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.BlackList;
import com.windmeal.member.domain.Member;
import com.windmeal.member.dto.request.BlackListCreateRequest;
import com.windmeal.member.dto.request.BlackListDeleteRequest;
import com.windmeal.member.exception.BlackListNotFoundException;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.BlackListRepository;
import com.windmeal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlackListService {

  private final BlackListRepository blackListRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public void addBlackList(Long requestMemberId, BlackListCreateRequest request){
    Member requestMember = memberRepository.findById(requestMemberId)
        .orElseThrow(MemberNotFoundException::new);

    Member blackMember = memberRepository.findByEmail(request.getEmail())
        .orElseThrow(MemberNotFoundException::new);

    BlackList blackList = BlackList.place(requestMember, blackMember);
    blackListRepository.save(blackList);
  }

  @Transactional
  public void deleteBlackList(Long requestMemberId, BlackListDeleteRequest request){
    Member requestMember = memberRepository.findById(requestMemberId)
        .orElseThrow(MemberNotFoundException::new);

    BlackList blackList = blackListRepository.findById(request.getBlackListId())
        .orElseThrow(MemberNotFoundException::new);

    blackListRepository.delete(blackList);
  }

  
}
