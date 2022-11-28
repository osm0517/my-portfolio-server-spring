package com.example.community.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @ApiParam(value = "아이디")
    private String userId;

    @ApiParam(value = "이메일")
    private String email;

    @ApiParam(value = "비밀번호")
    private String password;

    @ApiParam(value = "이름")
    private String name;

    @ApiParam(value = "닉네임")
    private String nickname;

    @ApiParam(value = "권한")
    private String auth;

}
