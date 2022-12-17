package com.example.community.controller;

import com.example.community.dto.Comment;
import com.example.community.dto.Report;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

//    url list
    private String BASE_URL = "/comment";
    private String createURL = "/create";
    private String deleteURL = "/delete";
    private String reportURL = "/report";
    private String listURL = "/list";
    private String totalURL = "/total";

//    variable list
    private String userId = "testId";
    private long boardId = 1;
    private String comment = "testComment";
    private String commentId = "1";
    private String reportType = "testType";

    @Test
    @DisplayName("글에 댓글을 추가(내용 넣기)")
    void createComment() throws Exception {
        String body = mapper.writeValueAsString(
                Comment.builder()
                        .userId(userId)
                        .boardId(boardId)
                        .comment(comment)
                        .build()
        );

        mvc.perform(post(BASE_URL+createURL)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글에 댓글을 추가(기본 내용 넣기)")
    void createCommentDefaultText() throws Exception {
        String body = mapper.writeValueAsString(
                Comment.builder()
                        .userId(userId)
                        .boardId(boardId)
                        .build()
        );

        mvc.perform(post(BASE_URL+createURL)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글에 댓글을 삭제")
    void deleteComment() throws Exception{
        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add("boardId", String.valueOf(boardId));
        parameter.add("commentId", commentId);

        mvc.perform(delete(BASE_URL+deleteURL)
                        .params(parameter))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글에 댓글을 신고")
    void reportComment() throws Exception {
        String body = mapper.writeValueAsString(
                Report.builder()
                        .reportType(reportType)
                        .commentId(Integer.parseInt(commentId))
                        .build()
        );

        mvc.perform(post(BASE_URL+reportURL)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 목록 불러오기")
    void searchComment() throws Exception {
        mvc.perform(get(BASE_URL+listURL)
                        .param("boardId", String.valueOf(boardId)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글에 댓글 총 개수를 불러옴")
    void getCommentTotal() throws Exception {
        mvc.perform(get(BASE_URL+totalURL)
                        .param("boardId", String.valueOf(boardId)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}