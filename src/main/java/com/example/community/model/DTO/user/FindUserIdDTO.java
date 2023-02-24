package com.example.community.model.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindUserIdDTO {

    private String email;
    private String name;
}
