package com.example.community.controller;

import com.example.community.dto.Board;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

//  bodyData List
    private String userId = "testUserId";
    private int categoryId = 1;
    private String title = "testTitle";
    private String boardText = "testTestTest";
//  URL List
    private String BASE_URL = "/board";
    private String writeURL = "/write";
    private String detailURL = "/read/detail";
    private String updateURL = "/update";
    private String readURL = "/read";
    private String totalURL = "/total";
    private String deleteURL = "/delete";
//  parameter List
    private String boardIdValue = "1";
//    삭제돼서 안될 수 있음 업데이트를 해야함
    private String updateBoardId = "10";
    private String page = "1";
    private String category = "1";
    private String sortType = "latest";
    private String searchQuery = "test";

    @Test
    @DisplayName("글 쓰기(성공)")
    void writeSuccess() throws Exception{

        String body = mapper.writeValueAsString(
                Board.builder()
                        .userId(userId)
                        .title(title)
                        .categoryId(categoryId)
                        .boardText(boardText)
                        .build()
        );

        mvc.perform(
                post(BASE_URL + writeURL)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 상세정보를 가져옴(성공)")
    void detailSuccess() throws Exception {

        mvc.perform(
                        get(BASE_URL + detailURL)
                                .param("boardId", boardIdValue)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("작성한 글 업데이트(성공)")
    void updateSuccess() throws Exception{
        String body = mapper.writeValueAsString(
                Board.builder()
                        .title(title)
                        .categoryId(categoryId)
                        .boardText(boardText)
                        .build()
        );

        mvc.perform(
                        post(BASE_URL + updateURL)
                                .content(body)
                                .param("boardId", updateBoardId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 목록 불러오기(성공)")
    /*현재 상태는 sortType = latest(최신순) page = 1
    searchQuery = test category = 1
     */
    void readSuccess() throws Exception{
        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add("page", page);
        parameter.add("category", category);
        parameter.add("sortType", sortType);
        parameter.add("searchQuery", searchQuery);
        mvc.perform(
                        get(BASE_URL + readURL)
                                .params(parameter)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물의 총 개수를 확인(성공)")
    void findTotalByAllSuccess() throws Exception{
        mvc.perform(
                        get(BASE_URL + totalURL)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물을 삭제(성공)")
    void deleteSuccess() throws Exception{
        String deleteId = "502";
        mvc.perform(
                        delete(BASE_URL + deleteURL)
                                .param("boardId", boardIdValue)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}