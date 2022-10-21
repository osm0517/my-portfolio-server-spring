package com.example.community.controller;

import com.example.community.dto.User;
import com.example.community.utils.BCryptService;
import com.example.community.repository.UserRepository;
import com.example.community.utils.BCryptService;
import com.example.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private BCryptService bCryptService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository repository;

    @RequestMapping("/signup")
    public String  signup(@RequestBody User user){
        /*현재 에러 처리는 service에서 try catch 문으로 처리
        * 기본 처리만 한 것이기에 세세하게 만들 때에는 다시 해야함*/
        return userService.create(user);
    }

    @RequestMapping("/test")
    public String dd(@RequestBody User user) throws Exception {
//        try{
//            repository.selectPick(email);
//        }catch (NullPointerException e) {
//            return "nullPointerException";
//        }
        String result = bCryptService.encodeBcrypt(user.getPassword(), 10);

        System.out.println(user.getPassword().getClass().getName());
        System.out.println(result);
        return result;
    }

    @RequestMapping("/login")
    public String login(@RequestBody User user){
        /* 기본 단계지만 학습을 위해서 service에서 발생한 에러를
        * 현재 위치(controller)로 전송함
        * 여기서 에러를 처리*/
        String email = user.getEmail();
        String password = user.getPassword();
        try{
            return userService.search(email, password);
        }catch (Exception e){
            String error = e.getClass().toString();
            System.out.println(error);
            return error;
        }
    }

    @RequestMapping("/search")
    public String search(@RequestBody User user){


        return "dddd";
    }

    @RequestMapping("/change")
    public String change(@RequestBody User user){


        return "dddd";
    }

    @RequestMapping("/delete")
    public String delete(@RequestBody User user){


        return "dddd";
    }

    @RequestMapping("/overlap")
    public String overlap(@RequestBody User user){


        return "dddd";
    }

    @RequestMapping("/auth")
    public String auth(@RequestBody User user){


        return "dddd";
    }

}
