package com.example.community.model.DAO.board;

import com.example.community.data.category.Category;
import com.example.community.model.DAO.user.User;
import com.example.community.model.DTO.post.PostEditDTO;
import io.swagger.annotations.ApiParam;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity @Table(name = "board")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ApiParam(value = "제목")
    @Column(name = "title", nullable = false)
    private String title;

    @ApiParam(value = "내용")
    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ApiParam(value = "분류")
    @Column(name = "category", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @ApiParam(value = "상세분류")
    @Column(name = "detail_category", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category detailCategory;

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
    @Column(name = "count", insertable = false)
    private long count;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<Comment>();

    public Post(String title, String text, User user, Category category, Category detailCategory) {
        this.title = title;
        this.text = text;
        this.user = user;
        this.category = category;
        this.detailCategory = detailCategory;
    }

    public Post editPost(PostEditDTO postEditDTO) throws IllegalAccessException{
        this.title = postEditDTO.getTitle();
        this.text = postEditDTO.getText();
        this.category = postEditDTO.getCategory();
        this.detailCategory = postEditDTO.getDetailCategory();

        return this;
    }

}
