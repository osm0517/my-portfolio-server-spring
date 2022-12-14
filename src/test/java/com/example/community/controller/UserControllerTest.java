package com.example.community.controller;

import com.example.community.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 어떤 class를 테스트하는지 명시
 * service와 repository를 사용하려면
 * 해당 annotation으로는 안됨
 */
//@WebMvcTest(UserController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    /**
     * controller test에 시작점
     * method를 날릴 수 있음
     */
    @Autowired
    MockMvc mvc;

    private String BASE_URL = "/user";

    @Test
    @DisplayName("함 테스트 해보자")
    void save_test() throws Exception {
        //given
        String userId = "jUnitTest";
        String email = "test";
        String auth = "USER";
        String name = "seongmin";
        String nickname = "hahaha";
        String password = "test";
        //when
        /**
         * Object를 JSON으로 변환
         * */
        String body = mapper.writeValueAsString(
                User.builder()
                        .userId(userId)
                        .email(email)
                        .auth(auth)
                        .name(name)
                        .nickname(nickname)
                        .password(password)
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + "/signup")
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andExpect(content().string("SIGNUP OK"));
    }
}