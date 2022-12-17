package com.example.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BoardCountControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private String BASE_URL = "/count";

    private String successBoardId = "5";

    private String failBoardId = "-1";

    @Test
    @DisplayName("조회수가 올라가는 로직(성공)")
    void plusSuccess() throws Exception {


        mvc.perform(
                post(BASE_URL + "/" + successBoardId)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("조회수를 조회하는 로직(성공)")
    void selectSuccess() throws Exception {

        mvc.perform(
                        get(BASE_URL + "/" + successBoardId)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("조회수가 올라가는 로직(실패)")
    void plusFail() throws Exception {

        mvc.perform(
                        post(BASE_URL + "/" + failBoardId)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("조회수를 조회하는 로직(실패)")
    void selectFail() throws Exception {

        mvc.perform(
                        get(BASE_URL + "/" + failBoardId)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}