package com.example.community.service;

import com.example.community.dto.Auth;
import com.example.community.dto.Response;
import com.example.community.dto.User;
import com.example.community.repository.CountRepository;
import com.example.community.repository.UserRepository;
import com.example.community.utils.BCryptService;
import com.example.community.utils.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Objects;

@Service
public class CountService {

    @Autowired
    CountRepository repository;

    //조회수를 카운팅하기 위해서 사용
    public Response plus(Long boardId){
        Response result = new Response();
        try {
            int plusResult = repository.plusCount(boardId);

            if(plusResult != 1) throw new Error("plusCount Error");

            result.setMessage("정상적으로 카운트 됨");
        }catch (Exception e){
            String error = e.toString();
            System.out.println(error);
            result.setMessage(error);
        }
        return result;
    }

    //조회수를 조회하기 위해서 사용함
    public Response select(Long boardId){
        Response result = new Response();
        try {
            Long selectResult = repository.selectCount(boardId);

            result.setMessage("정상적으로 조회 됨");
            result.setData(selectResult);
        }catch (Exception e){
            String error = e.toString();
            System.out.println(error);
            result.setMessage(error);
        }
        return result;
    }
}
