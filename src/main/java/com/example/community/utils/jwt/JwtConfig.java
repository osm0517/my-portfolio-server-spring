package com.example.community.utils.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtConfig {

    @Value("${jwt.access-secretKey}")
    private String accessSecretKey;

    @Value("${jwt.refresh-secretKey}")
    private String refreshSecretKey;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

//    private final CustomUserDetailService customUserDetails;

//    @PostConstruct
//    protected void init() {
//        accessSecretKey = Base64.getEncoder().encodeToString(accessSecretKey.getBytes());
//    }

    //access token 값을 반환하는 메소드
    public String createAccessToken(String userId, List<String> roleList) {
        Claims claims = Jwts.claims().setSubject(userId); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roleList); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + accessExpiration)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, accessSecretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    //refresh token 값을 반환하는 메소드
    public String createRefreshToken(String userId, List<String> roleList) {
        Claims claims = Jwts.claims().setSubject(userId); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roleList); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + refreshExpiration)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String userId = Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token).getBody().getSubject();
//        UserDetails userDetails = customUserDetails.loadUserByUsername(userId);
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken("", "", null);
    }

//    public Boolean testAuth(String token) {
//        Jws<Claims> claims = Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token);
//        return claims.getBody().getExpiration().before(new Date());
//    }

    // Request의 Header에서 cookie 값을 가져옵니다.
    // 가져온 쿠키 값을 split으로 잘라서 X-AUTH-TOKEN 값을 추출
    public String resolveToken(HttpServletRequest request) {
        log.debug("get cookie => {}", request.getHeader("cookie"));
        try{
            String value = request.getHeader("cookie");
            String[] a = value.split("; ");
            log.debug("contains => {}", Arrays.asList(a).contains("X-AUTH-TOKEN"));
            String[] b = new String[100];
            for(int i = 0; i < a.length; i++){
                b = a[i].split("=");
                if(Arrays.asList(b).contains("X-AUTH-TOKEN")) break;
            }
            return b[1];
        }
        catch (NullPointerException e){
            log.warn("resolveToken Null");
            return null;
        }
        catch (ExpiredJwtException e){
            log.info("expired token");
            return null;
        }
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException | MalformedJwtException e) {
            log.info("JWT validateToken exception => {}", e.getMessage());
            return false;
        }
    }

}