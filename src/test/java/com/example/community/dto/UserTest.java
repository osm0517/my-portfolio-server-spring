package com.example.community.dto;

import com.example.community.model.DAO.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

@SpringBootTest
class UserTest {

    @Test
    @DisplayName("reflection test")
    void reflection() throws IllegalAccessException {
        User user = new User("aaa", "Aaa", "aaaa", "aaaaa");

        for(Field field : user.getClass().getDeclaredFields()){
            field.setAccessible(true);
            System.out.println(field.getName() + " = " + field.get(user));
        }
    }
}