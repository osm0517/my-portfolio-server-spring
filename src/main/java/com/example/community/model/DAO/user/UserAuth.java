package com.example.community.model.DAO.user;

import io.swagger.annotations.ApiParam;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter @ToString
@NoArgsConstructor
@Entity @Table(name = "user_auth")
public class UserAuth {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ApiParam(value = "이메일")
    @Column(name = "email", nullable = false)
    private String email;

    @ApiParam(value = "랜덤한 문자열")
    @Column(name = "random_string", nullable = false)
    private String randomString;

    @ApiParam(value = "시도한 횟수")
    @Column(name = "number", insertable = false)
    private Integer number;

    @ApiParam(value = "마지막 수정 날짜")
    @Column(name = "updated_date", updatable = false)
    private Date updatedDate;

    public UserAuth(String email, String randomString){
        this.email = email;
        this.randomString = randomString;
    }

    public void update(String randomString, int number){
        this.randomString = randomString;
        this.number = number;
    }

    public void changeUpdatedDate(Date date){
        this.updatedDate = date;
    }
}
