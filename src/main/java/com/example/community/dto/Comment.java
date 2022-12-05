package com.example.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private long boardId;

    private long commentId;

    private String userId;

    private String comment;
}
