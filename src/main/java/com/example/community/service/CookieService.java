package com.example.community.service;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.List;

@Component
public class CookieService {

    public Cookie createCookie(String key, String value, long time, String path){
        Cookie cookie = new Cookie(key, value);

        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) time);
        cookie.setDomain("localhost");
        cookie.setPath(path);

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

    //header 중에 특정 key가 존재하는지 찾아줌
    public Boolean searchCookieByKey(String header, String key) {
        try{
            String[] splitList = header.split("=");

            for(String i : splitList){
                if(i.contains(key)){
                    return true;
                }
            }
            return false;
        }//들어온 문자가 없다면 nullpointerexception
        catch (NullPointerException e){
            return false;
        }
    }

    //userId를 추출해서 반환
    public String searchUserIdByCookie(String token, String accessSecretKey){
        return Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
