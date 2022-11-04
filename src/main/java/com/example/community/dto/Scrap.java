package com.example.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Scrap {


    private Long boardId;

    //스크랩을 해가는 사람의 id
    private Long UserId;
}
