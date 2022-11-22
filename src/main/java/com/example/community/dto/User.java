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

    private String email;

    private String password;

    private String name;

    private String nickname;

    private String auth;

}
