package com.example.community.model.DAO.report;

import com.example.community.model.DAO.board.Comment;
import com.example.community.model.DAO.user.User;
import io.swagger.annotations.ApiParam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity @Table(name = "comment_report")
public class CommentReport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User reporter;

    @ApiParam(value = "신고 유형")
    @Column(name = "report_type", nullable = false)
    private String reportType;

    @ApiParam(value = "신고 내용")
    @Column(name = "text")
    private String text;

    public CommentReport(Comment comment, User reporter, String reportType, String text) {
        this.comment = comment;
        this.reporter = reporter;
        this.reportType = reportType;
        this.text = text;
    }
}
