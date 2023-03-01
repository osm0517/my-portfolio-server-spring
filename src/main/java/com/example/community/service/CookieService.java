package com.example.community.service;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class CookieService {

    public Cookie createCookie(String key, String value, long time, String path){
        Cookie cookie = new Cookie(key, value);

        if(path == null){
            path = "/";
        }

        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) time);
        cookie.setPath(path);

        return cookie;
    }

    public Cookie loginCheckCookie(long time){
        Cookie cookie = new Cookie("login-check-cookie", "login");

        cookie.setMaxAge((int) time);
        cookie.setDomain("localhost");
        cookie.setPath("/");

        return cookie;
    }

    //userId를 추출해서 반환
    public String searchUserIdByCookie(String token, String accessSecretKey){
        return Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
