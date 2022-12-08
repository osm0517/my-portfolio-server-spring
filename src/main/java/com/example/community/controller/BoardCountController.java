package com.example.community.controller;


import com.example.community.dto.Response;
import com.example.community.service.CookieService;
import com.example.community.service.CountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController()
@Api(tags = {"조회수를 관리하는 API"})
@RequestMapping("/count")
public class BoardCountController {

    @Autowired
    CountService countService;

    @Autowired
    CookieService cookieService;

    //조회를 했는지 체크하는 cookie에 시간으로 1일 유지를 함
    long oneDayTime = 24*60*60*1000;

    String visitCheckCommonPath = "/board/read/detail/";

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test(HttpServletRequest request){
        System.out.println("testCookie => " + request.getHeader("Cookie"));
        String[] test = request.getHeader("Cookie").split("=");
        for(String i : test){
            if(i.contains("Cookie_4")){
                System.out.println("testValue => " + List.of(test).contains("Cookie_4"));
            }
        }

    }
    //게시물의 조회수를 증가시키기 위해서 사용함
    @ApiOperation(value = "조회수 증가", notes = "가장 처음 글이 만들어지면" +
            "조회수가 0인 값을 만들고 조회를 할 때 마다 1씩 증가시키는 로직으로 구성" +
            "//cookie path 속성에 따라서 조회를 했는지를 판단하는 cookie를 전송해서" +
            "체크함")
    @RequestMapping(value = "/{boardId}", method = RequestMethod.POST)
    public ResponseEntity<?> plus(@PathVariable long boardId, HttpServletResponse servletResponse,
                                  HttpServletRequest servletRequest){

        String cookieString = servletRequest.getHeader("Cookie");

        Response response = countService.plus(boardId, cookieString);

        Object checkCookie = response.getData();
        if(!Objects.equals(checkCookie, null)){
            Cookie cookie = cookieService.createCookie((String)checkCookie, "value", oneDayTime
            ,visitCheckCommonPath+"boardId");
            servletResponse.addCookie(cookie);
        }
        HttpStatus status = response.getStatus();

        return new ResponseEntity<>(response, status);
    }

    //게시물의 조회수를 조회하기 위해서 사용함
    @ApiOperation(value = "조회수를 조회할 때 사용")
    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET)
    public ResponseEntity<?> select(@PathVariable long boardId){

        Response response = countService.select(boardId);
        HttpStatus status = response.getStatus();

        return new ResponseEntity<>(response, status);
    }
}
