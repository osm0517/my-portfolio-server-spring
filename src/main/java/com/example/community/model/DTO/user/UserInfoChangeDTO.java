package com.example.community.model.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class UserInfoChangeDTO {

    private String password;
    private String confirmPassword;
    private String name;
    private String email;
}
