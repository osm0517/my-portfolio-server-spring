package com.example.community.service;

import com.example.community.dto.Auth;
import com.example.community.dto.Response;
import com.example.community.dto.User;
import com.example.community.repository.UserRepository;
import com.example.community.utils.BCryptService;
import com.example.community.utils.Consistency;
import com.example.community.utils.MailService;
import com.example.community.utils.jwt.JwtConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserService {

    private int strength= 10;

    @Autowired
    private BCryptService bCryptService;

    @Autowired
    private Consistency consistency;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 중복확인을 할 때 사용하는 메소드
     * false 면 중복확인 실패
     * true 면 중복확인 성공
     * **/
    private boolean overlapServeMethod(String type, User user){
        String email = user.getEmail();
        String userId = user.getUserId();
        String nickname = user.getNickname();

        switch (type) {
            case "email" :
                return !Objects.equals(email, null);
            case "userId" :
                return !Objects.equals(userId, null);
            case "nickname" :
                return !Objects.equals(nickname, null);
            default:
                log.error("BAD OVERLAP TYPE = {}", type);
                return false;
        }
    }

    public Response signup(User user) throws DuplicateKeyException {
        String inputPassword = user.getPassword();
        String inputEmail = user.getEmail();
        Response result = new Response();

//        email 정합성 확인
        if(!consistency.emailConsistency(inputEmail)){
            result.setStatus(HttpStatus.BAD_REQUEST);
            result.setMessage("EMAIL BAD INPUT");

            log.debug("BAD EMAIL = {}", inputEmail);
            return result;
        }

        //hash 알고리즘을 사용해서 비밀번호를 암호화
        String hashPassword = bCryptService.encodeBcrypt(inputPassword, strength);
        //반드시 db에는 암호화가 이루어진 비밀번호를 저장
        user.setPassword(hashPassword);

        int signupResult = repository.signup(user);
        //만약 1이 아니면 정상적으로 이루어진게 아님
        if(signupResult == 1){
            log.debug("signupSuccessResult = {}", signupResult);
            result.setStatus(HttpStatus.OK);
            result.setMessage("SIGNUP OK");
        }else{
            log.debug("signupFailResult = {}", signupResult);
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("SIGNUP FAIL");
        }

        return result;
    }

    public Response login(String userId, String password, String auth){
        Response response = new Response();

        if(Objects.equals(userId, null) || Objects.equals(password, null) || Objects.equals(auth, null)){
            response.setMessage("Bad Request Data");
            response.setStatus(HttpStatus.BAD_REQUEST);
        }else {

            String searchResultPassword = repository.selectPassword(userId);

            boolean passwordMatch = bCryptService.matchesBcrypt(password, searchResultPassword, strength);

            if (passwordMatch) {
                response.setMessage("OK");
                response.setStatus(HttpStatus.OK);
            } else {
                response.setMessage("Wrong UserInformation");
                response.setStatus(HttpStatus.UNAUTHORIZED);
            }
        }
        return response;
    }

//    public Response search(String type, String email)
//            throws Exception{
//        Response response = new Response();
//
//        if(Objects.equals(email, null)){
//            response.setMessage("Bad Request Data");
//            response.setStatus(HttpStatus.BAD_REQUEST);
//        }else {
//            switch (type) {
//                case "password":
//                    String result = repository.selectPassword(email);
//                    if (Objects.equals(result, null)) {
//                        response.setStatus(HttpStatus.OK);
//                        response.setMessage("Password Not Found");
//                    } else {
//                        response.setStatus(HttpStatus.OK);
//                        response.setMessage("OK");
//                        response.setData(result);
//                    }
//
//                case "email":
//                    return repository.selectEmail(email);
//
//                default:
//                    return "잘못된 type이 들어옴";
//            }
//        }
//    }

    public Response change(User user){
        String password = user.getPassword();
        String name = user.getName();
        String nickname = user.getNickname();

        Response response = new Response();

        if(Objects.equals(password,null) && Objects.equals(name,null) && Objects.equals(nickname,null)){
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Input Data Not Found");
        }

        //만약 비밀번호를 변경한다면
        if(!Objects.equals(password, null)){
            password = bCryptService.encodeBcrypt(password, strength);
            user.setPassword(password);
        }
        repository.change(user);

        response.setStatus(HttpStatus.OK);
        response.setMessage("OK");

        return response;
    }

    public Response delete(String userId){
        Response response = new Response();

        //입력된 아이디가 존재하지 않을 때
        if(Objects.equals(userId, null)){
            response.setMessage("userId Data Not Input");
            response.setStatus(HttpStatus.BAD_REQUEST);
        }else{

            String selectResult = repository.selectPassword(userId);

            log.debug("delete method userId = {}", userId);
            log.debug("delete method select user information = {}", selectResult);

            //해당하는 아이디가 없을 때
            if(Objects.equals(selectResult, null)){
                response.setMessage("USER Data Not Exist");
                response.setStatus(HttpStatus.CONFLICT);
            }else{
                repository.delete(userId);

                //성공적으로 지우면 redirect로 홈 화면으로 나갈 수 있도록 함
                response.setStatus(HttpStatus.OK);
            }
        }
        return response;
    }

    //중복확인
    public Response overlap(String type, User user){
        Response response = new Response();

        if(!Objects.equals(type, "email") && !Objects.equals(type, "nickname") && !Objects.equals(type, "userId")){
            response.setMessage("Wrong Type Value");
            response.setStatus(HttpStatus.BAD_REQUEST);
        }

        else {
            String email = user.getEmail();

            if(!Objects.equals(user.getEmail(), null)) {

//        email 정합성 확인
                if (!consistency.emailConsistency(email)) {
                    response.setStatus(HttpStatus.BAD_REQUEST);
                    response.setMessage("EMAIL BAD INPUT");

                    log.debug("BAD EMAIL = {}", email);
                    return response;
                }
            }
            if(!overlapServeMethod(type, user)){
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("TYPE, DATA NOT MATCH");

                log.info("TYPE DATA NOT MATCH = {}", type);
                return response;
            }
            //중복확인 값
            String selectValue = repository.infoOverlap(user);

            //중복확인 통과
            if (Objects.equals(selectValue, null)) {
                response.setMessage("OK");
                response.setStatus(HttpStatus.OK);
            }
            //중복확인에서 충돌
            //이메일이 존재한다 -> 이미 사용자가 가입을 했었다.
            else {
                response.setMessage("Already Exist Information");
                response.setStatus(HttpStatus.CONFLICT);
                response.setData(selectValue);
            }
        }
            return response;
    }

    //인증 메일 전송하는 메소드
    //기존에 인증확인을 위한 randomString이 존재한다면 기존 것을 삭제
    public Response mail(String email){
        Response response = new Response();

        //        email 정합성 확인
        if(!consistency.emailConsistency(email)){
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("EMAIL BAD INPUT");

            log.debug("BAD EMAIL = {}", email);
            return response;
        }

        String randomString = mailService.createCode();

        mailService.sendMail(randomString, email, "tjdals990517@naver.com");

        //이미 보낸 메일이 있으면 randomString을 삭제하고 저장
        int selectResult = repository.countAuth(email);

        if(selectResult != 0){ repository.deleteAuth(email); }
        //메일 보내는데 성공하면 db에 인증 정보를 저장
        repository.createAuth(new Auth(email, randomString));
        response.setStatus(HttpStatus.OK);
        response.setMessage("OK");

        return response;
    }

    public Response auth(Auth auth){

        String randomString = auth.getRandomstring();
        String email = auth.getEmail();

        Response response = new Response();

        int countAuth = repository.countAuth(email);

        //보낸 인증메일이 존재
        if(countAuth > 0){
            String selectAuth = repository.selectAuth(email);

            boolean matchCode = selectAuth.contains(randomString);

            //입력값과 db에 저장된 값이 같음
            if(matchCode){

                //입력값과 db에 값이 동일해서 삭제를 한다면
                int deleteResult = repository.deleteAuth(email);
                //삭제 값이 1이라면
                if(deleteResult == 1) {
                    response.setMessage("OK");
                    response.setStatus(HttpStatus.OK);

                    log.debug("auth randomString delete success");
                }
            }else {
                response.setMessage("Input Random String Wrong");
                response.setStatus(HttpStatus.CONFLICT);
            }
        }
        //보낸 인증메일이 존재하지 않음
        else {
            response.setMessage("Data Not Exist To DB");
            response.setStatus(HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
