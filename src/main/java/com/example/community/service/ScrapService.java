//package com.example.community.service;
//
//import com.example.community.dto.Response;
//import com.example.community.model.DAO.board.Scrap;
//import com.example.community.repository.board.ScrapRepository;
////import com.example.community.utils.jwt.CustomUserDetailService;
//import com.example.community.utils.jwt.JwtConfig;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//
//@Service
//@Slf4j
//public class ScrapService {
//
//    @Autowired
//    ScrapRepository repository;
//
////    @Autowired
////    CustomUserDetailService userDetailService;
//
//    @Autowired
//    JwtConfig jwtConfig;
//
//    @Autowired
//    CookieService cookieService;
//
//    @Value("${jwt.access-secretKey}")
//    private String accessSecretKey;
//
//    public Response create(Long boardId, HttpServletRequest request){
//
//        Response result = new Response();
//
//        //header에서 access쿠키 값을 가져옴
//        String token = jwtConfig.resolveToken(request);
//        //해당 값에서 userId값을 가져옴
//        String userId = cookieService.searchUserIdByCookie(token, accessSecretKey);
//
//
//        result.setMessage("OK");
//        result.setStatus(HttpStatus.OK);
//
//        return result;
//    }
//
//    public Response delete(long boardId, HttpServletRequest request){
//
//        Response result = new Response();
//
//        //header에서 access쿠키 값을 가져옴
//        String token = jwtConfig.resolveToken(request);
//        //해당 값에서 userId값을 가져옴
//        String userId = cookieService.searchUserIdByCookie(token, accessSecretKey);
//
//
//        result.setMessage("OK");
//        result.setStatus(HttpStatus.OK);
//
//        return result;
//    }
//
//    public Response count(Scrap scrap){
//
//        Response result = new Response();
//        long value;
//
//        String countResult = repository.count(scrap);
//
//        if(Objects.equals(countResult, null)) throw new Error("Scrap Delete Error");
//
//        value = Long.parseLong(repository.count(scrap));
//        result.setMessage("스크랩 개수를 정상적으로 조회함");
//        result.setData(value);
//
//        return result;
//    }
//}
