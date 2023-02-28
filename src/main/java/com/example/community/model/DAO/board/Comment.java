package com.example.community.model.DAO.board;

import com.example.community.model.DAO.user.User;
import io.swagger.annotations.ApiParam;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;


@Getter
@NoArgsConstructor
@Entity @Table(name = "comment")
@DynamicInsert // insert query를 보낼 때 null인 값을 제외해서 default 값을 보내줌
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Post post;

    @ApiParam(value = "내용")
    @Column(name = "comment")
    @ColumnDefault("너무 좋아요:)")
    private String text;

    @ApiParam(value = "신고 횟수")
    @Column(name = "number_of_report", insertable = false)
    private int report;

    public Comment(User user, Post post, String text) {
        this.user = user;
        this.post = post;
        this.text = text;
    }
}
