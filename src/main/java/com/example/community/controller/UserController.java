package com.example.community.controller;

import com.example.community.data.ROLE;
import com.example.community.model.DTO.user.FindUserIdDTO;
import com.example.community.model.DTO.user.UserLoginDTO;
import com.example.community.model.DTO.user.UserSignupDTO;
import com.example.community.model.VO.ErrorVO;
import com.example.community.model.VO.UserLoginResultVO;
import com.example.community.model.VO.UserSignupResultVO;
import com.example.community.service.CookieService;
import com.example.community.service.UserService;
import com.example.community.utils.jwt.JwtConfig;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.*;

@RestController
@RequestMapping("/user")
@Api(tags = {"user 관련 API"})
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtConfig jwtConfig;
    private final CookieService cookieService;

    @Value("${jwt.access-token-name}")
    private String accessTokenName;
    @Value("${jwt.refresh-token-name}")
    private String refreshTokenName;
    @Value("${jwt.access-expiration}")
    private long accessExpiration;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody UserLoginDTO userLoginDTO,
            HttpServletResponse response
    ){

        try {
            UserLoginResultVO login = userService.login(userLoginDTO);

            List<ROLE> roles = new ArrayList<>(Collections.singleton(ROLE.USER));

            String refreshToken = jwtConfig.createRefreshToken(userLoginDTO.getUserId(), roles);
            String accessToken = jwtConfig.createAccessToken(userLoginDTO.getUserId(), roles);

            response.addCookie(cookieService.createCookie(refreshTokenName, refreshToken, refreshExpiration, null));
            response.addCookie(cookieService.createCookie(accessTokenName, accessToken, accessExpiration, null));

            return new ResponseEntity<>(login, HttpStatus.OK);
        }catch (IllegalArgumentException e){

            if(e.getMessage().equals("password not match for confirm")){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }else{
                return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            }

        }catch (NoSuchElementException e){
            return new ResponseEntity<>("user not found with id", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Validated @RequestBody UserSignupDTO userSignupDTO,
            BindingResult bindingResult
    ){

        if(bindingResult.hasErrors()){
            return getErrorVOResponseEntity(bindingResult);
        }

        try {
            UserSignupResultVO savedUser = userService.signup(userSignupDTO);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        }catch (DataIntegrityViolationException e){

            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

        }
    }

    @GetMapping("/find/id")
    public ResponseEntity<?> find(
            @Validated @ModelAttribute FindUserIdDTO findUserIdDTO,
            BindingResult bindingResult
    ){

        if(bindingResult.hasErrors()){
            return getErrorVOResponseEntity(bindingResult);
        }

        try {

            String findUserId = userService.findUserId(findUserIdDTO);

            if(findUserId == null){
                return new ResponseEntity<>(findUserId, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(findUserId, HttpStatus.CREATED);
            }

        }catch (DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private static ResponseEntity<ErrorVO> getErrorVOResponseEntity(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();

        List<String> errorList = new ArrayList<>();

        for (FieldError error : errors) {
            String value = Arrays.stream(error.getCodes()).toList().get(1);

            errorList.add(value);
        }

        ErrorVO error = ErrorVO.builder()
                .errorMessage("validation error")
                .errors(Collections.singletonList(errorList))
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
