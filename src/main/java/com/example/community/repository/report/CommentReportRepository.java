package com.example.community.repository.report;

import com.example.community.model.DAO.report.BoardReport;
import com.example.community.model.DAO.report.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
}
