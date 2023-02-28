package com.example.community.model.DTO.comment;

import com.example.community.data.report.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentReportDTO {

    private Long userId;
    private Long postId;
    private Long commentId;
    private String reportType;
    private String text;

}
