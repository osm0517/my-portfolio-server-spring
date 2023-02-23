package com.example.community.model.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginDTO {

    private String userId;
    private String password;
    private String confirmPassword;
}
