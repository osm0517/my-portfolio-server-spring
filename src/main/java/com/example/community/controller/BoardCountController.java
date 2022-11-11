package com.example.community.controller;


import com.example.community.dto.Response;
import com.example.community.dto.Test;
import com.example.community.service.CountService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController()
@RequestMapping("/count")
public class BoardCountController {

    @Autowired
    CountService countService;

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void test(@RequestBody Test test){
        System.out.println(test);
    }
    //게시물의 조회수를 증가시키기 위해서 사용함
    @RequestMapping(value = "/{boardId}", method = RequestMethod.POST)
    public ResponseEntity<?> plus(@PathVariable int boardId){
        Long id = (long) boardId;
        Response response = new Response();
        HttpStatus status = HttpStatus.OK;

        try {
            response = countService.plus(id);
        }catch (Exception e){
            String error = e.toString();
            System.out.println(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }

        return new ResponseEntity<>(response, status);
    }

    //게시물의 조회수를 조회하기 위해서 사용함
    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET)
    public ResponseEntity<?> select(@PathVariable int boardId){
        Long id = (long) boardId;
        Response response = new Response();
        HttpStatus status = HttpStatus.OK;

        try {
            response = countService.select(id);
        }catch (Exception e){
            String error = e.toString();
            System.out.println(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }
}
