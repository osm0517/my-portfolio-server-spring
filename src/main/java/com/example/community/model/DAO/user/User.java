package com.example.community.model.DAO.user;

import io.swagger.annotations.ApiParam;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity @Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_user_id_email_unique",
                        columnNames = {"user_id", "email"}
                )
        }
)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ApiParam(value = "아이디")
    @Column(name = "user_id", nullable = false)
    private String userId;

    @ApiParam(value = "비밀번호")
    @Column(name = "password", nullable = false)
    private String password;

    @ApiParam(value = "이름")
    @Column(name = "name", nullable = false)
    private String name;

    @ApiParam(value = "이메일")
    @Column(name = "email", nullable = false)
    private String email;

    @ApiParam(value = "정지 여부")
    @Column(name = "stop_state", insertable = false)
    private String stopState;

    @ApiParam(value = "탈퇴 여부")
    @Column(name = "delete_state", insertable = false)
    private String deleteState;

    @ApiParam(value = "생성 날짜")
    @Column(name = "created_date", nullable = false, insertable = false, updatable = false)
    private Date createdDate;

    @ApiParam(value = "수정 날짜")
    @Column(name = "updatedDate", nullable = false, insertable = false, updatable = false)
    private Date updatedDate;

    @ApiParam(value = "신고 받은 횟수")
    @Column(name = "number_of_report", insertable = false)
    private int report;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    /**
     * Test 진행을 할 때 편하기 위해서 사용하는 것임
     * 사용할 때 주의하기
     */
    public void changePassword(String password){
        this.password = password;
    }

}
