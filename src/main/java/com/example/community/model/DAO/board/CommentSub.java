package com.example.community.model.DAO.board;

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
@Entity @Table(name = "comment_sub")
public class CommentSub {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //    일단 보류
//    private User user;

    //    일단 보류
//    private Comment comment;

    @ApiParam(value = "내용")
    @Column(name = "comment", nullable = false)
    private String text;

    @ApiParam(value = "신고 횟수")
    @Column(name = "number_of_report", insertable = false)
    private int report;
}
