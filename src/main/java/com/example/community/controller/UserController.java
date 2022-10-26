package com.example.community.controller;

import com.example.community.dto.Auth;
import com.example.community.dto.User;
import com.example.community.utils.BCryptService;
import com.example.community.repository.UserRepository;
import com.example.community.utils.BCryptService;
import com.example.community.service.UserService;
import com.example.community.utils.MailService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String  signup(@RequestBody User user){
        /*현재 에러 처리는 service에서 try catch 문으로 처리
        * 기본 처리만 한 것이기에 세세하게 만들 때에는 다시 해야함*/
        return userService.signup(user);
    }

    @RequestMapping("/test")
    public List<String> dd(@RequestBody Auth auth) {
        System.out.println(auth.getEmail());
        return repository.selectAuth(auth.getEmail());
    }

    @RequestMapping("/login")
    public String login(@RequestBody User user){
        /* 기본 단계지만 학습을 위해서 service에서 발생한 에러를
        * 현재 위치(controller)로 전송함
        * 여기서 에러를 처리*/
        String email = user.getEmail();
        String password = user.getPassword();
        try{
            return userService.login(email, password);
        }catch (Exception e){
            String error = e.getClass().toString();
            System.out.println(error);
            return error;
        }
    }

    @RequestMapping("/search/{type}")
    public String search(@PathVariable String type,@RequestBody User user){

        String email = user.getEmail();
        try {
            return userService.search(type, email);
        }catch (Exception e){
            String error = e.getClass().toString();
            System.out.println(error);
            return error;
        }
    }

    @RequestMapping("/change")
    public String change(@RequestBody User user){
        try{
            return userService.change(user);
        }catch (Exception e){
            String error = e.getClass().toString();
            System.out.println(error);
            return error;
        }
    }

    @RequestMapping("/delete")
    public String delete(@RequestBody User user){
        try {
            return userService.delete(user);
        }catch (Exception e){
            String error = e.getClass().toString();
            System.out.println(error);
            return error;
        }
    }

    @RequestMapping("/overlap")
    public String overlap(@RequestBody User user){
        try {
            return userService.overlap(user.getEmail());
        }catch (Exception e){
            System.out.println(e);
            return e.toString();

        }
    }

    @RequestMapping("/auth")
    public String auth(@RequestBody Auth auth){

        try {
            return userService.auth(auth);
        }catch (Exception e){
            String error = e.getClass().toString();
            System.out.println(error);
            return error;
        }
    }
}
