package com.example.community.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignupDTO {

    private String userId;
    private String password;
    private String name;
    private String email;
//    필수 약관
    private boolean termsOfInfo;
    private boolean termsOfExam;
//    선택 약관
    private boolean termsOfSelect;
}
