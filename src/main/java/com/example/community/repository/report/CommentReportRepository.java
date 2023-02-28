package com.example.community.repository.report;

import com.example.community.model.DAO.board.Post;
import com.example.community.model.DAO.report.CommentReport;
import com.example.community.model.DAO.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    List<CommentReport> findAllByReporter(User reporter);

}
