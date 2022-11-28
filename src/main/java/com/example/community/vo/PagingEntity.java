package com.example.community.vo;

import lombok.*;

@Getter
@AllArgsConstructor
public class PagingEntity {

    private int gap;

    private int listTotal;

    private String sortType;

    private int categoryId;

    private String searchQuery;

}
