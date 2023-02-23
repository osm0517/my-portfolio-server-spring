package com.example.community.model.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDeleteDTO {

    private long id;
    private String password;
    private String confirmPassword;
}
