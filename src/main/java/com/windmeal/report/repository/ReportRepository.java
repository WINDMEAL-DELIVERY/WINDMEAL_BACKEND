package com.windmeal.report.repository;

import com.windmeal.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> ,ReportCustomRepository{

}
