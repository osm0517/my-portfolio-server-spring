package com.example.community.model.DTO.post;

import com.example.community.data.category.Category;
import com.example.community.model.DAO.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;

@Getter
@AllArgsConstructor
public class PostWriteDTO {

    private String title;
    private String text;
    private User user;
    private Category category;
    private Category detailCategory;

    public boolean hasNull() throws IllegalAccessException {
        for(Field field : this.getClass().getDeclaredFields()){
            if(field.get(this) == null){
                return true;
            }
        }
        return false;
    }
}
