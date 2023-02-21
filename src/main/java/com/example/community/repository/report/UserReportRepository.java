package com.example.community.repository.report;

import com.example.community.model.DAO.report.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
}
