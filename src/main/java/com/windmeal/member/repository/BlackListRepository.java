package com.windmeal.member.repository;

import com.windmeal.member.domain.BlackList;
import com.windmeal.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList,Long>,BlackListCustomRepository {

}
