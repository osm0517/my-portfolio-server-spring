package com.example.community.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginDTO {

    private String userId;
    private String password;
}
