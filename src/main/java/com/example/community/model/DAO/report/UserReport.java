package com.example.community.model.DAO.report;

import com.example.community.model.DAO.user.User;
import io.swagger.annotations.ApiParam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "user_report")
public class UserReport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    일단 보류
//    private User user;

    @ApiParam(value = "신고 유형")
    @Column(name = "report_type", nullable = false)
    private int reportType;

    @ApiParam(value = "신고 내용")
    @Column(name = "text")
    private String text;
}
