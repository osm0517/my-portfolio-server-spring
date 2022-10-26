package com.example.community.service;

import com.example.community.dto.User;
import com.example.community.repository.UserRepository;
import com.example.community.utils.BCryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private int strength= 10;

    @Autowired
    private BCryptService bCryptService;

    @Autowired
    private UserRepository repository;

    /*먼저 기본적인 api의 틀을 잡기 위해서
    출력을 성공했다 실패했다는 문자열을 보냄*/
    public String signup(User user) {
        String inputPassword = user.getPassword();
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

    public String login(String email, String password) throws Exception{
        String searchResultPassword = repository.selectPassword(email);

        boolean passwordMatch = bCryptService.matchesBcrypt(password, searchResultPassword, strength);

        if(passwordMatch) return "db 정보와 일치함";
        else return "db에 해당하는 정보가 없음";
    }

    public String search(String type, String email) throws Exception{
        switch (type) {
            case "password" :
                return repository.selectPassword(email);

            case "email" :
                return repository.selectEmail(email);

            default:
                return "잘못된 type이 들어옴";
        }
    }

    public String change(User user) throws Exception{
        repository.change(user);
        return "정상적으로 회원 정보가 수정됨";
    }

    public String delete(User user) throws Exception{
        String inputPassword = user.getPassword();
        String inputEmail = user.getEmail();
        String dbPassword = repository.selectPassword(inputEmail);

        if(Objects.equals(dbPassword, null)) return "해당 이메일은 존재하지 않는 이메일입니다.";

        //비밀번호가 맞는지 확인함
        boolean matchResult = bCryptService.matchesBcrypt(inputPassword, dbPassword, strength);

        if(matchResult) {
            repository.delete(inputEmail);
            return "db에서 데이터를 정상적으로 삭제함";
        }else return "비밀번호가 일치하지 않음";
    }
}
