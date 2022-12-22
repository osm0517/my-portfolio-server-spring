package com.example.community.controller;

import com.example.community.dto.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    private String deleteURL = "/delete";
    private String overlapURL = "/overlap";
    private String mailURL = "/overlap/mail";
    private String authURL = "/auth";

//    variable list
    private String userId = "userId";
    private String testId = "111";
    private String password = "kt1079616";
    private String auth = "USER";
    private String name = "testName";
    private String nickname = "testNickname";
    private String email = "testEmail";
    private String authEmail = "okt0517@gmail.com";

    @Test
    @DisplayName(value = "회원가입 로직")
    @Order(1)
    void signup() throws Exception {
        Date date = new Date();
        String dateStr = date.toString();
        String password = "testPwd";

        String body = mapper.writeValueAsString(
                User.builder()
                        .userId(testId)
                        .password(password)
                        .name(name)
                        .nickname(userId)
                        .email(dateStr + "@gmail.com")
                        .auth(auth)
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + signupURL)
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 로직")
    @Order(2)
    void login() throws Exception {
        userId = "dhtjdals77";

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

//    userId를 인식하지 못해서 테스트가 계속해서 실패
    @Test
    @DisplayName("계정 삭제")
    @Order(3)
    void deleteTest() throws Exception {
        MultiValueMap<String , String > body = new LinkedMultiValueMap<>();
        body.add("userId", testId);
        String request = mapper.writeValueAsString(body);
        //then
        mvc.perform(delete(BASE_URL + deleteURL)
                        .content(request) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("중복확인(아이디)")
    void overlapId() throws Exception {
        String body = mapper.writeValueAsString(
                User.builder()
                        .userId("rmsidehofk")
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + overlapURL + "/userId")
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("중복확인(닉네임)")
    void overlapNick() throws Exception {
        String body = mapper.writeValueAsString(
                User.builder()
                        .nickname("rmsidehofk")
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + overlapURL + "/nickname")
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("중복확인(이메일)")
    void overlapEmail() throws Exception {
        Date date = new Date();
        String nowStr = date.toString();
        String body = mapper.writeValueAsString(
                User.builder()
                        .email(nowStr+"@gmail.com")
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + overlapURL + "/email")
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

//    url parameter = userId
//    input value = email
    @Test
    @DisplayName("중복확인(실패)")
    void overlapFail() throws Exception {
        String body = mapper.writeValueAsString(
                User.builder()
                        .email("hahaha@gmail.com")
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + overlapURL + "/userId")
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

//    email 정합성이 깨짐
//    input value = email
    @Test
    @DisplayName("중복확인(메일 정합성 실패)")
    void overlapMailFail() throws Exception {
        String body = mapper.writeValueAsString(
                User.builder()
                        .email("rmsidehofk")
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + overlapURL + "/email")
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("인증메일 전송(성공)")
    void overlapMailPost() throws Exception {
        String body = mapper.writeValueAsString(
                User.builder()
                        .email(authEmail)
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + mailURL)
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

//    정상적인 email을 입력하지 않음 -> ***@***.*** 형식을 지키지 않음
    @Test
    @DisplayName("인증메일 전송(실패)")
    void overlapMailPostFail() throws Exception {
        String body = mapper.writeValueAsString(
                User.builder()
                        .email("okdhsod")
                        .build()
        );
        //then
        mvc.perform(post(BASE_URL + mailURL)
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("인증번호 인증(실패)")
    void auth() throws Exception {
        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add("email",authEmail);
        parameter.add("randomString",authEmail);

        mvc.perform(get(BASE_URL + authURL)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}