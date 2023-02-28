package com.example.community.model.DTO.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;

@Getter
@AllArgsConstructor
public class CommentWriteDTO {

    private Long userId;
    private Long postId;
    private String text;

    public boolean hasNull() throws IllegalAccessException {
        for (Field field : this.getClass().getDeclaredFields()){
            if(field.get(this) == null && !field.getName().equals("text")){
                return true;
            }
        }
        return false;
    }
}
