package com.example.community.controller;

import com.example.community.dto.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

//    url list
    private String BASE_URL = "/user";
    private String signupURL = "/signup";
    private String loginURL = "/login";
    private String changeURL = "/change";

//    variable list
    private String userId = "dhtjdals77";
    private String password = "kt1079616";
    private String auth = "USER";
    private String name = "testName";
    private String nickname = "testNickname";

    @Test
    @DisplayName(value = "회원가입 로직")
    void signup() throws Exception {

        String userId = "testId";
        String password = "testPwd";
        String email = "testEmail";


        String body = mapper.writeValueAsString(
                User.builder()
                        .userId(userId)
                        .password(password)
                        .name(name)
                        .nickname(email)
                        .email(nickname)
                        .auth(auth)
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + signupURL)
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 로직")
    void login() throws Exception {

        String body = mapper.writeValueAsString(
                User.builder()
                        .userId(userId)
                        .password(password)
                        .auth(auth)
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + loginURL)
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원정보를 수정(비밀번호)")
    void changePwd() throws Exception {
        String body = mapper.writeValueAsString(
                User.builder()
                        .password(password)
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + changeURL)
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원정보를 수정(이름)")
    void changeName() throws Exception {
        String body = mapper.writeValueAsString(
                User.builder()
                        .name(name)
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + changeURL)
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원정보를 수정(닉네임)")
    void changeNick() throws Exception {
        String body = mapper.writeValueAsString(
                User.builder()
                        .nickname(nickname)
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + changeURL)
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원정보를 수정(전부)")
    void changeAll() throws Exception {
        String body = mapper.writeValueAsString(
                User.builder()
                        .name(name)
                        .password(password)
                        .nickname(nickname)
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + changeURL)
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("계정 삭제")
    void delete() {
    }

    @Test
    @DisplayName("중복확인")
    void overlap() {
    }

    @Test
    @DisplayName("인증메일 전송")
    void overlapMailPost() {
    }

    @Test
    @DisplayName("인증번호 인증")
    void auth() {
    }
}