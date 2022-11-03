package com.example.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    private String title;

    private Integer userId;

    private Integer categoryId;

    private Integer stackId;

    private String boardText;
}
