package com.example.community.model.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
@AllArgsConstructor
public class UserLoginResultVO {

    private String userId;
    private String name;

}
