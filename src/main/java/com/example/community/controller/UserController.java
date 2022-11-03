package com.example.community.controller;

import com.example.community.dto.Auth;
import com.example.community.dto.Response;
import com.example.community.dto.User;
import com.example.community.utils.BCryptService;
import com.example.community.repository.UserRepository;
import com.example.community.utils.BCryptService;
import com.example.community.service.UserService;
import com.example.community.utils.MailService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private BCryptService bCryptService;
    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository repository;

    @RequestMapping("/signup")
    public Response signup(@RequestBody User user){
        /*현재 에러 처리는 service에서 try catch 문으로 처리
        * 기본 처리만 한 것이기에 세세하게 만들 때에는 다시 해야함*/
        Response response = new Response();
        response.setMessage(userService.signup(user));
        return response;
    }

    @RequestMapping("/test")
    public String dd(@RequestBody int a, HttpServletResponse response) throws IOException {

        Exception test = null;
        System.out.println(test.toString());
        if(a == 1) response.sendError(401, "error");
        else if(a == 2) response.sendError(402, "error");
        else if(a == 3) response.sendError(403, "error");
        else if(a == 4) return "되자 제발..!";
        return "뭐임 ㅠ";
    }


    @RequestMapping("/login")
    public Response login(@RequestBody User user){
        /* 기본 단계지만 학습을 위해서 service에서 발생한 에러를
        * 현재 위치(controller)로 전송함
        * 여기서 에러를 처리*/
        Response response = new Response();

        String email = user.getEmail();
        String password = user.getPassword();
        try{
            response.setMessage(userService.login(email, password));
            return response;
        }catch (Exception e){
            System.out.println(e);
            response.setMessage(e.toString());
            return response;
        }
    }

    @RequestMapping("/search/{type}")
    public Response search(@PathVariable String type,@RequestBody User user){

        Response response = new Response();

        String email = user.getEmail();
        try {
            response.setMessage(userService.search(type, email));
            return response;
        }catch (Exception e){
            System.out.println(e);
            response.setMessage(e.toString());
            return response;
        }
    }

    @RequestMapping("/change")
    public Response change(@RequestBody User user){

        Response response = new Response();

        try{
            response.setMessage(userService.change(user));
            return response;
        }catch (Exception e){
            System.out.println(e);
            response.setMessage(e.toString());
            return response;
        }
    }

    @RequestMapping("/delete")
    public Response delete(@RequestBody User user){

        Response response = new Response();

        try {
            response.setMessage(userService.delete(user));
            return response;
        }catch (Exception e){
            System.out.println(e);
            response.setMessage(e.toString());
            return response;
        }
    }

    @RequestMapping("/overlap")
    public Response overlap(@RequestBody User user){

        Response response = new Response();

        try {
            response.setMessage(userService.overlap(user.getEmail()));
            return response;
        }catch (Exception e){
            System.out.println(e);
            response.setMessage(e.toString());
            return response;

        }
    }

    @RequestMapping("/auth")
    public Response auth(@RequestBody Auth auth){

        Response response = new Response();

        try {
            response.setMessage(userService.auth(auth));
            return response;
        }catch (Exception e){
            System.out.println(e);
            response.setMessage(e.toString());
            return response;
        }
    }

    //error handler
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullException(NullPointerException a){
        Response result = new Response();
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
