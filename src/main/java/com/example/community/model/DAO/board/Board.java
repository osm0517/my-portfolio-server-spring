package com.example.community.model.DAO.board;

import com.example.community.model.DAO.user.User;
import io.swagger.annotations.ApiParam;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "board")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    일단 보류
//    private User user;

    @ApiParam(value = "제목")
    @Column(name = "title", nullable = false)
    private String title;

    @ApiParam(value = "내용")
    @Column(name = "text", nullable = false)
    private String text;

    @ApiParam(value = "분류")
    @Column(name = "category", nullable = false)
    private String category;

    @ApiParam(value = "상세분류")
    @Column(name = "detail_category", nullable = false)
    private String detailCategory;

    @ApiParam(value = "생성 날짜")
    @Column(name = "created_date", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @ApiParam(value = "수정 날짜")
    @Column(name = "updated_date", nullable = false, insertable = false, updatable = false)
    private Date updatedDate;

    @ApiParam(value = "신고 받은 횟수")
    @Column(name = "number_of_report", insertable = false)
    private int report;

    @ApiParam(value = "스크랩 된 횟수")
    @Column(name = "scrap_count", insertable = false)
    private long scrapCount;

    @ApiParam(value = "조회수")
    @Column(name = "count", updatable = false, insertable = false)
    private long count;
}
