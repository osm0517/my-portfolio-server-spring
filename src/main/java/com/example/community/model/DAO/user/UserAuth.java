package com.example.community.model.DAO.user;

import io.swagger.annotations.ApiParam;
import jakarta.persistence.*;
import lombok.*;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private int number;
}
