package com.example.community.service;

import com.example.community.dto.Auth;
import com.example.community.dto.Response;
import com.example.community.dto.User;
import com.example.community.repository.CountRepository;
import com.example.community.repository.UserRepository;
import com.example.community.utils.BCryptService;
import com.example.community.utils.MailService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Objects;

@Service
public class CountService {

    @Autowired
    CountRepository repository;

    @Autowired
    CookieService cookieService;

    //조회수를 카운팅하기 위해서 사용
    public Response plus(Long boardId, String cookieString){
        Response result = new Response();
        String visitCheckCookie = "VISIT_CHECK_COOKIE";
        try {
            //request Cookie header에 해당 cookie가 있는지 확인함
            Boolean checkVisitByCookie = cookieService.searchCookieByKey(cookieString, visitCheckCookie);
            //존재하지 않는다면 db에서 조회수를 1 증가시킴
            if(Objects.equals(checkVisitByCookie, false)) {
                int plusResult = repository.plusCount(boardId);

                if (plusResult != 1) throw new Error("plusCount Error");
                //다시 증가시키지 않기 위해서 cookie에 key값을 controller로 넘겨줌
                result.setData(visitCheckCookie);
            }else {
                //존재하다면 key값을 넘겨주지 않음
                result.setData(null);
            }
            //cookie에 존재 유뮤와는 상관없이 badRequest가 아니기 때문에 200번을 응답함
            result.setMessage("OK");
            result.setStatus(HttpStatus.OK);
        }catch (Exception e){
            String error = e.toString();
            System.out.println(error);
            result.setMessage(error);
        }
        return result;
    }

    //조회수를 조회하기 위해서 사용함
    public Response select(Long boardId)
        throws Exception {
        Response response = new Response();
        Long selectResult = repository.selectCount(boardId);

        response.setMessage("OK");
        response.setData(selectResult);
        response.setStatus(HttpStatus.OK);
        return response;
    }
}
