package com.windmeal.member.repository;

import com.windmeal.member.domain.MemberReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberReportRepository extends JpaRepository<MemberReport,Long> , MemberReportCustomRepository{

}
