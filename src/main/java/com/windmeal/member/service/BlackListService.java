package com.windmeal.member.service;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.BlackList;
import com.windmeal.member.domain.Member;
import com.windmeal.member.dto.request.BlackListCreateRequest;
import com.windmeal.member.dto.request.BlackListDeleteRequest;
import com.windmeal.member.dto.response.BlackListResponse;
import com.windmeal.member.exception.BlackListNotFoundException;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.BlackListRepository;
import com.windmeal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  public Page<BlackListResponse> getBlackList(Long requestMemberId, Pageable pageable) {
    Member member = memberRepository.findById(requestMemberId).orElseThrow(
        MemberNotFoundException::new);
    return blackListRepository.getBlackListByRequesterId(pageable, member.getId());
  }

  @Transactional
  public void deleteBlackList(Long requestMemberId, Long blackListId){
    memberRepository.findById(requestMemberId)
        .orElseThrow(MemberNotFoundException::new);
    BlackList blackList = blackListRepository.findById(blackListId)
        .orElseThrow(MemberNotFoundException::new);
    blackListRepository.delete(blackList);
  }


}
