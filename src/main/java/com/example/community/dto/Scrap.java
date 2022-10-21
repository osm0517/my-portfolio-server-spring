package com.example.community.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Scrap {

    private Integer id;

    private Integer boardId;

    private Integer UserId;
}
