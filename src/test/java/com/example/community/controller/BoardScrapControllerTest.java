package com.example.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BoardScrapControllerTest {
//    현재 스크랩을 진행할 때에 jwt가 유효한지를 먼저 확인을 해서
//    정상적으로 테스트가 불가능

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

//    url을 정리
    private String BASE_URL = "/scrap";

//    변수를 정리
    private String boardIdValue = "1";

    @Test
    @DisplayName("스크랩을 생성(성공)")
    void createSuccess() throws Exception {

        mvc.perform(
                post(BASE_URL+"/1")
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("스크랩을 취소(성공)")
    void deleteSuccess() throws Exception {
        mvc.perform(
                        delete(BASE_URL+"/1")
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}