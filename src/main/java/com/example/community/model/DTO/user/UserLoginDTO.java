package com.example.community.model.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {

    private String userId;
    private String password;
    private String confirmPassword;


}
