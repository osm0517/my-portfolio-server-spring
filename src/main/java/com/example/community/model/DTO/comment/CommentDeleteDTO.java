package com.example.community.model.DTO.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDeleteDTO {

    private Long userId;
    private Long postId;
    private Long commentId;

}
