package com.example.community.service;

import com.example.community.dto.User;
import com.example.community.repository.UserRepository;
import com.example.community.utils.BCryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private BCryptService bCryptService;

    @Autowired
    private UserRepository repository;

    /*먼저 기본적인 api의 틀을 잡기 위해서
    출력을 성공했다 실패했다는 문자열을 보냄*/
    public String create(User user) {
        String inputPassword = user.getPassword();
        int strength = 10;
        try{
            String hashPassword = bCryptService.encodeBcrypt(inputPassword, strength);

            user.setPassword(hashPassword);
            int signupResult = repository.signup(user);

            System.out.println(signupResult);
            return "정상적으로 db에 정보가 올라감";
        }catch (Exception e){
            System.out.println("createError => " + e.getClass().toString());
            return "user_tb에 데이터를 저장하는 중에 에러 발생";
        }
    }

    public String search(String email, String password) throws Exception{
        String searchResultPassword = repository.selectPassword(email);

        boolean passwordMatch = bCryptService.matchesBcrypt(password, searchResultPassword, 10);

        if(passwordMatch) return "db 정보와 일치함";
        else return "db에 해당하는 정보가 없음";
    }
}
