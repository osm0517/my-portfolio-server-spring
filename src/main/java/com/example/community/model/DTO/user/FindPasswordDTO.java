package com.example.community.model.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindPasswordDTO {

    private String userId;
    private String email;
}
