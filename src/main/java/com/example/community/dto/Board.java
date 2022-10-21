package com.example.community.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Board {

    private Integer id;

    private String title;

    private Integer userId;

    private Integer categoryId;

    private Integer stackId;
}
