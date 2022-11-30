package com.example.community.utils.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtConfig {

    @Value("${jwt.access-secretKey}")
    private String accessSecretKey;

    @Value("${jwt.refresh-secretKey}")
    private String refreshSecretKey;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Autowired
    private  CustomUserDetailService customUserDetails;

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
        UserDetails userDetails = customUserDetails.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Boolean testAuth(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token);
        return claims.getBody().getExpiration().before(new Date());
    }

    // Request의 Header에서 cookie 값을 가져옵니다.
    // 가져온 쿠키 값을 split으로 잘라서 X-AUTH-TOKEN 값을 추출
    public String resolveToken(HttpServletRequest request) {
        System.out.println("accessKey JwtConfig => " + accessSecretKey);
        System.out.println("resolveToken => " + request.getHeader("cookie"));
        try{
            String value = request.getHeader("cookie");
            String[] a = value.split("; ");
            System.out.println("a => " + Arrays.toString(a));
            int val = Arrays.asList(a).indexOf("X-AUTH-TOKEN");
            System.out.println("============================");
            System.out.println("val = > " + val);
            System.out.println("contains => " + Arrays.asList(a).contains("X-AUTH-TOKEN"));
            System.out.println("============================");
            String[] b = new String[100];
            System.out.println("a.length => " + a.length);
            for(int i = 0; i < a.length; i++){
                System.out.println(Arrays.toString(a[i].split("=")));
                b = a[i].split("=");
                if(Arrays.asList(b).contains("X-AUTH-TOKEN")) break;
            }
            System.out.println("b => " + Arrays.toString(b));
            System.out.println("return resolveToken => "+ b[0]);
            return b[1];
        }
        catch (NullPointerException e){
            System.out.println("null");
            return null;
        }
        catch (ExpiredJwtException e){
            System.out.println("token 만료");
            return null;
        }
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException | MalformedJwtException e) {
            System.out.println("==========");
            System.out.println("JWT validateToken exception => "+ e.getMessage());
            System.out.println("==========");
            return false;
        }
    }

}