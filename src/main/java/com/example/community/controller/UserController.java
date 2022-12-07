package com.example.community.controller;

import com.example.community.dto.Auth;
import com.example.community.dto.Response;
import com.example.community.dto.User;
import com.example.community.service.CookieService;
import com.example.community.utils.BCryptService;
import com.example.community.repository.UserRepository;
import com.example.community.utils.BCryptService;
import com.example.community.service.UserService;
import com.example.community.utils.MailService;
import com.example.community.utils.jwt.JwtConfig;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/user")
@Api(tags = {"user 관련 API"})
@RequiredArgsConstructor
@Slf4j
public class UserController {
    @Autowired
    private BCryptService bCryptService;
    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private JwtConfig jwtConfig;


    @Value("${jwt.access-secretKey}")
    private String accessKey;
    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;


    //일단은 완벽하게 짜놓음
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ApiOperation(value = "회원가입", notes = "2022.11.22 기준 권한을 어떻게 설정하여서" +
            "DB에 저장해야할지 로직을 구성하지 못함 -> 해당 내용을 추가해야할 것 같음")
    public ResponseEntity<?> signup(@RequestBody User user){

        /*현재 에러 처리는 service에서 try catch 문으로 처리
        * 기본 처리만 한 것이기에 세세하게 만들 때에는 다시 해야함*/
        Response response = userService.signup(user);
        HttpStatus status = response.getStatus();

        return new ResponseEntity<>(response, status);
    }



    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Object  dd(HttpServletRequest request) {
        //header에서 access쿠키 값을 가져옴
        String token = jwtConfig.resolveToken(request);
        //해당 값에서 userId값을 가져옴
        String userId = cookieService.searchUserIdByCookie(token, accessKey);
        return userId;
    }


    //지금 생각으로 완벽하게 짬
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "로그인", notes = "2022.11.22 기준 로그인 시에" +
            "refresh token, access token, login check cookie를 제공" +
            "다시 한 번 손을 봐야할 듯 싶음")
    public ResponseEntity<?> login(@RequestBody User user,
                                   HttpServletResponse servletResponse){
        /* 기본 단계지만 학습을 위해서 service에서 발생한 에러를
        * 현재 위치(controller)로 전송함
        * 여기서 에러를 처리*/

        String userId = user.getUserId();
        String password = user.getPassword();
        String auth = user.getAuth();

        Response response = userService.login(userId, password, auth);
        HttpStatus status = response.getStatus();

        if(status == HttpStatus.OK){
            String accessToken = jwtConfig.createAccessToken(userId, Arrays.asList("ROLE_USER"));
            String refreshToken = jwtConfig.createRefreshToken(userId, Arrays.asList("ROLE_USER"));


            Cookie accessCookie = cookieService.createCookie("X-AUTH-TOKEN", accessToken, accessExpiration, "/");
            Cookie refreshCookie = cookieService.createCookie("RE-AUTH-TOKEN", refreshToken, refreshExpiration, "/");
            //httponly 설정이 안 된 cookie 로그인이 됐는지 클라이언트에서 확인하기 위함
            Cookie loginCheckCookie = cookieService.loginCheckCookie(accessExpiration);

            servletResponse.addCookie(accessCookie);
            servletResponse.addCookie(refreshCookie);
            servletResponse.addCookie(loginCheckCookie);
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
    @ApiOperation(value = "정보 수정", notes = "회원정보를 수정하는 로직")
    public ResponseEntity<?> change(@RequestBody User user){

        Response response = userService.change(user);
        HttpStatus status = response.getStatus();

        return new ResponseEntity<>(response, status);
    }

    //지금 상태로는 완벽
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ApiOperation(value = "정보 수정")
    public ResponseEntity<?> delete(@RequestBody String email){
        HttpStatus status;

        Response response = userService.delete(email);

        //정상적으로 삭제되면 홈 화면으로 redirect
        if(Objects.equals(response.getStatus(), HttpStatus.OK)){
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/"));
            status = HttpStatus.MOVED_PERMANENTLY;
            return new ResponseEntity<>(headers, status);
        }

        status = response.getStatus();
        return new ResponseEntity<>(response, status);
    }

    //지금 생각으로 완벽
    @RequestMapping(value = "/overlap/{type}", method = RequestMethod.POST)
    @ApiOperation(value = "중복확인", notes = "회원가입시에 정보가 중복되었는지" +
            "확인하기 위해 사용하는 api로 ID, NICKNAME, EMAIL을 확인할 수 있음")
    public ResponseEntity<?> overlap(@PathVariable String type, @RequestBody User user){

        log.debug("overlap user information = {}", user);

        Response response = userService.overlap(type, user);
        HttpStatus status = response.getStatus();

        return new ResponseEntity<>(response, status);
    }

    //이메일로 인증메일을 보냄
    //지금 생각으로는 완벽
    @RequestMapping(value = "/overlap/mail", method = RequestMethod.POST)
    @ApiOperation(value = "인증메일 전송", notes = "email을 중복확인을 한 후에" +
            "해당 메일이 실제로 존재하는지 와 해당 메일이 실제로 사용자가 사용하는 메일인지를" +
            "확인하기 위해서 메일을 전송함")
    public ResponseEntity<?> overlapMailPost(@RequestBody String email){

        log.debug("overlapMail email = {}", email);

        Response response = userService.mail(email);
        HttpStatus status = response.getStatus();

        return new ResponseEntity<>(response, status);
    }

    //지금으로는 완벽
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    @ApiOperation(value = "인증번호 인증", notes = "사용자가 메일로 받은 인증번호를" +
            "인증하기 위해서 사용함")
    public ResponseEntity<?> auth(@RequestParam(name = "email") String email,
                                  @RequestParam(name = "randomString") String randomString){

        log.debug("auth to email = {}", email);
        log.debug("auth randomString = {}", randomString);

        Response response = userService.auth(new Auth(email, randomString));
        HttpStatus status = response.getStatus();

        return new ResponseEntity<>(response, status);
    }
}
