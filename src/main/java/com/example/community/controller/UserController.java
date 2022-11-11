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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
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

    //일단은 완벽하게 짜놓음
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signup(@RequestBody User user){

        /*현재 에러 처리는 service에서 try catch 문으로 처리
        * 기본 처리만 한 것이기에 세세하게 만들 때에는 다시 해야함*/
        Response response = userService.signup(user);
        HttpStatus status = response.getStatus();

        return new ResponseEntity<>(response, status);
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


    //지금 생각으로 완벽하게 짬
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user){
        /* 기본 단계지만 학습을 위해서 service에서 발생한 에러를
        * 현재 위치(controller)로 전송함
        * 여기서 에러를 처리*/
        Response response = new Response();
        HttpStatus status;

        String email = user.getEmail();
        String password = user.getPassword();
        try{
            response = userService.login(email, password);

            status = response.getStatus();
        }catch (Exception e){
            System.out.println("==========");
            System.out.println("UserController Login Method Catch Exception");
            System.out.println("Login Error => " + e);
            System.out.println("==========");

            response.setMessage("UserController Login Method Catch Exception");
            response.setData(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }

    //정보 찾는건 일단 빼기
//    @RequestMapping(value = "/search/{type}", method = RequestMethod.GET)
//    public ResponseEntity<?> search(@PathVariable String type,@RequestBody User user){
//
//        Response response = new Response();
//
//        String email = user.getEmail();
//        try {
//            response.setMessage(userService.search(type, email));
//            return response;
//        }catch (Exception e){
//            System.out.println(e);
//            response.setMessage(e.toString());
//            return response;
//        }
//    }

    //일단은 완벽
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    public ResponseEntity<?> change(@RequestBody User user){

        Response response = new Response();
        HttpStatus status;

        try{
            response = userService.change(user);
            status = response.getStatus();
            
        }catch (Exception e){
            System.out.println("==========");
            System.out.println("UserController Change Method Catch Exception");
            System.out.println("Change Error => " + e);
            System.out.println("==========");

            response.setMessage("UserController Change Method Catch Exception");
            response.setData(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }

    //지금 상태로는 완벽
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestBody String email){
        Response response = new Response();
        HttpStatus status;

        try {
            response = userService.delete(email);

            //정상적으로 삭제되면 홈 화면으로 redirect
            if(Objects.equals(response.getStatus(), HttpStatus.OK)){
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create("/"));
                status = HttpStatus.MOVED_PERMANENTLY;
                return new ResponseEntity<>(headers, status);
            }

            status = response.getStatus();
        }catch (Exception e){
            System.out.println("==========");
            System.out.println("UserController Delete Method Catch Exception");
            System.out.println("Delete Error => " + e);
            System.out.println("==========");

            response.setMessage("UserController Delete Method Catch Exception");
            response.setData(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }

    //지금 생각으로 완벽
    @RequestMapping(value = "/overlap/{type}", method = RequestMethod.GET)
    public ResponseEntity<?> overlap(@PathVariable String type, @RequestBody User user){

        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status;

        try {
            response = userService.overlap(type, user);
            status = response.getStatus();

            //type = email & 반환값이 존재한다 -> 이미 사용자가 가입을 했다
            if(Objects.equals(type, "email") && Objects.equals(response.getStatus(), HttpStatus.CONFLICT)){
                headers.setLocation(URI.create("/user/search"));
                status = HttpStatus.MOVED_PERMANENTLY;

                return new ResponseEntity<>(headers,status);
            }
        }catch (Exception e){
            System.out.println("==========");
            System.out.println("UserController Overlap Method Catch Exception");
            System.out.println("Overlap Error => " + e);
            System.out.println("==========");

            response.setMessage("UserController Overlap Method Catch Exception");
            response.setData(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }

    //이메일로 인증메일을 보냄
    //지금 생각으로는 완벽
    @RequestMapping(value = "/overlap/mail", method = RequestMethod.POST)
    public ResponseEntity<?> overlapMailPost(@RequestBody String email){

        Response response = new Response();
        HttpStatus status;

        try {
            response = userService.mail(email);
            status = response.getStatus();
        }catch (Exception e){

            System.out.println("==========");
            System.out.println("UserController overlapMail Method Catch Exception");
            System.out.println("overlapMail Error => " + e);
            System.out.println("==========");

            response.setMessage("UserController overlapMail Method Catch Exception");
            response.setData(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }

    //지금으로는 완벽
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ResponseEntity<?> auth(@RequestBody Auth auth){

        Response response = new Response();
        HttpStatus status;

        try {
            response = userService.auth(auth);
            status = response.getStatus();
        }catch (Exception e){
            System.out.println("==========");
            System.out.println("UserController Auth Method Catch Exception");
            System.out.println("Auth Error => " + e);
            System.out.println("==========");

            response.setMessage("UserController Auth Method Catch Exception");
            response.setData(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }

    //error handler
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullException(NullPointerException a){
        Response result = new Response();
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Error.class)
    public ResponseEntity<?> nullException(Error a){
        Response result = new Response();
        result.setMessage(a.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
