package com.example.community.service;

import com.example.community.data.ROLE;
import com.example.community.utils.jwt.JwtConfig;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CookieServiceTest {

    @Autowired
    CookieService cookieService;
    @Autowired
    JwtConfig jwtConfig;

    @Value("${jwt.access-token-name}")
    private String accessTokenName;
    @Value("${jwt.refresh-token-name}")
    private String refreshTokenName;
    @Value("${jwt.access-expiration}")
    private long accessExpiration;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Test
    void createCookie() {

        assertDoesNotThrow(() -> {
            List<ROLE> roles = new ArrayList<ROLE>(ROLE.USER.ordinal());

            String accessToken = jwtConfig.createAccessToken("aa", roles);

            cookieService.createCookie(accessTokenName, accessToken, accessExpiration, null);

            assertEquals("access", accessTokenName);
        });
    }

    @Test
    void loginCheckCookie() {
    }

    @Test
    void searchCookieByKey() {
    }

    @Test
    void searchUserIdByCookie() {
    }
}