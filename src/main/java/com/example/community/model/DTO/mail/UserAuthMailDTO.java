package com.example.community.model.DTO.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAuthMailDTO {

    private String email;
    private String randomString;
    private int number;
}
