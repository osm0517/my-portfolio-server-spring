package com.example.community.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @ApiParam(value = "글 제목")
    private String title;

    @ApiParam(value = "작성자 name")
    private String name;

    @ApiParam(value = "작성자 아이디")
    private Integer userId;

    @ApiParam(value = "글 분류 아이디")
    private Integer categoryId;

    @ApiParam(value = "글 상세 분류 아이디")
    private Integer detailCategoryId;

    @ApiParam(value = "글 내용")
    private String boardText;
}
