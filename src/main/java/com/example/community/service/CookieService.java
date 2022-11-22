package com.example.community.service;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class CookieService {

    public Cookie createCookie(String key, String value, long time){
        Cookie cookie = new Cookie(key, value);

        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) time);
        cookie.setDomain("localhost");
        cookie.setPath("/");

        //local 환경에서 테스트할 때는 https 환경이 안됨
        //그러므로 true를 하면 확인을 못함
        //테스트 끝나면 주석 풀기
//        cookie.setSecure(true);

        return cookie;
    }

    public Cookie loginCheckCookie(long time){
        Cookie cookie = new Cookie("login-check-cookie", "login");

        cookie.setMaxAge((int) time);
        cookie.setDomain("localhost");
        cookie.setPath("/");

        return cookie;
    }
}
