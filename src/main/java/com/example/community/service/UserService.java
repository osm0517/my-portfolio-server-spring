package com.example.community.service;

import com.example.community.dto.Auth;
import com.example.community.dto.User;
import com.example.community.repository.UserRepository;
import com.example.community.utils.BCryptService;
import com.example.community.utils.MailService;
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
    private MailService mailService;

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

            if(signupResult == 1){
                System.out.println(signupResult);
                return "정상적으로 db에 정보가 올라감";
            }else{
                System.out.println(signupResult);
                return "db에 정상적으로 정보가 올라가지 못함";
            }
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
                String result =  repository.selectPassword(email);
                if(Objects.equals(result, null)){
                    System.out.println(email + "에 해당하는 user 정보는 존재하지 않음");
                    return "해당하는 이메일이 존재하지 않음";
                }else{
                    System.out.println(email + "에 해당하는 비밀번호 -> " + result);
                    return "비밀번호 확인 후 교체하는 로직을 추가해보자";
                }

            case "email" :
                return repository.selectEmail(email);

            default:
                return "잘못된 type이 들어옴";
        }
    }

    public String change(User user) throws Exception{
        String password = user.getPassword();

        if(!Objects.equals(password, null)){
            password = bCryptService.encodeBcrypt(password, strength);
            user.setPassword(password);
            System.out.println("password encode => "+ password);
        }
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

    public String overlap(String toMail)
        throws Exception{

        String searchEmail = repository.selectEmail(toMail);
        int countAuth = repository.countAuth(toMail);

        String code = mailService.createCode();

        //하루에 가능한 조건을 3개로 제한함
        if(countAuth <= 3){
            if(Objects.equals(searchEmail, null)) {
                mailService.sendMail(code, toMail, "tjdals990517@naver.com");
                //메일이 전송되면 db에 저장하는 것을 바라고 프로그램 순서를 정함
                Auth authData = new Auth();
                authData.setEmail(toMail);
                authData.setRandomstring(code);

                int authResult = repository.createAuth(authData);

                if(authResult == 1) return "메일이 존재하지 않아 정상적으로 전송함";
                else return "auth code를 db에 저장하는 중 error가 발생하여 저장되지 않음";
            }

            else return "중복된 이메일이 존재함";
        }else{
            return "하루에 가능한 이메일 인증 횟수를 초과함";
        }
    }

    public String auth(Auth auth)
        throws Exception{

        String randomString = auth.getRandomstring();
        String email = auth.getEmail();

        int countAuth = repository.countAuth(email);

        if(countAuth > 0){
            List<String> selectAuth = repository.selectAuth(email);

            boolean matchCode = selectAuth.contains(randomString);

            if(matchCode) return "인증이 성공적으로 완료되었습니다.";
            return "인증번호가 일치하지 않습니다.";
        }

        else return "인증메일을 먼저 수령해주세요.";
    }
}
