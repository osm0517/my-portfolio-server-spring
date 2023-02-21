package com.example.community.repository.report;

import com.example.community.model.DAO.report.BoardReport;
import com.example.community.model.DAO.report.CommentSubReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentSubReportRepository extends JpaRepository<CommentSubReport, Long> {
}
