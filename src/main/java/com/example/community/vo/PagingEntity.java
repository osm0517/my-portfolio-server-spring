package com.example.community.vo;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public class PagingEntity {

    private int gap;

    private int range;

    private String sortType;

}
