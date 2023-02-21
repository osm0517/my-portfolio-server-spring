package com.example.community.repository.report;

import com.example.community.model.DAO.report.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {
}
