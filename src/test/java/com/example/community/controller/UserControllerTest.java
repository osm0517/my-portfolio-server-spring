package com.example.community.controller;

import com.example.community.model.DAO.user.User;
import com.example.community.model.DTO.user.FindUserIdDTO;
import com.example.community.model.DTO.user.UserLoginDTO;
import com.example.community.model.DTO.user.UserSignupDTO;
import com.example.community.model.VO.ErrorVO;
import com.example.community.model.VO.UserLoginResultVO;
import com.example.community.model.VO.UserSignupResultVO;
import com.example.community.repository.user.UserRepository;
import com.example.community.utils.jwt.JwtConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;

    @Value("${jwt.access-token-name}")
    private String accessTokenName;
    @Value("${jwt.refresh-token-name}")
    private String refreshTokenName;
    String userId = "seongmin";
    String email = "aa@gmail.com";
    String name = "Aa";
    User user = new User("aa", "aaaaaa1*", name, email);

    @BeforeEach
    void init(){
        String encodedPassword = bCryptPasswordEncoder.encode("aa");
        user.changePassword(encodedPassword);
        userRepository.save(user);
    }

    @AfterEach
    void clear(){
        userRepository.delete(user);

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if(optionalUser.isPresent()){
            User findUser = optionalUser.get();

            userRepository.delete(findUser);
        }
    }

    @Nested
    @DisplayName("로컬 로그인")
    class localLogin{

        @Test
        @DisplayName("성공 로직")
        void success() throws Exception {

            UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                    .userId("aa")
                    .password("aa")
                    .confirmPassword("aa")
                    .build();

            UserLoginResultVO userLoginResultVO = UserLoginResultVO.builder()
                    .name("Aa")
                    .userId("aa")
                    .build();

            String value = objectMapper.writeValueAsString(userLoginDTO);
            String result = objectMapper.writeValueAsString(userLoginResultVO);

            mockMvc.perform(
                    post("/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(value)
            ).andExpect(status().isOk())
                    .andExpect(content().json(result))
                    .andExpect(cookie().exists(accessTokenName))
                    .andExpect(cookie().exists(refreshTokenName))
                    .andDo(print());

        }

        @Test
        @DisplayName("실패 로직(password 불일치)")
        void fail1() throws Exception{
            UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                    .userId("aa")
                    .password("bb")
                    .confirmPassword("aa")
                    .build();

            String value = objectMapper.writeValueAsString(userLoginDTO);

            mockMvc.perform(
                    post("/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(value)
            ).andExpect(status().isUnauthorized())
                    .andExpect(content().string("password not match for user"))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 로직(없는 ID)")
        void fail2() throws Exception{
            UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                    .userId("bb")
                    .password("aa")
                    .confirmPassword("aa")
                    .build();

            String value = objectMapper.writeValueAsString(userLoginDTO);

            mockMvc.perform(
                            post("/user/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(value)
                    ).andExpect(status().isBadRequest())
                    .andExpect(content().string("user not found with id"))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 로직(password와 confirm 불일치)")
        void fail3() throws Exception{
            UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                    .userId("aa")
                    .password("aa")
                    .confirmPassword("bb")
                    .build();

            String value = objectMapper.writeValueAsString(userLoginDTO);

            mockMvc.perform(
                            post("/user/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(value)
                    ).andExpect(status().isBadRequest())
                    .andExpect(content().string("password not match for confirm"))
                    .andDo(print());
        }

    }

    @Nested
    @DisplayName("로컬 회원가입")
    class localSignup{

        @Test
        @DisplayName("성공 로직")
        void success() throws Exception{

            String name = "aa";

            UserSignupDTO userSignupDTO = UserSignupDTO.builder()
                    .userId(userId)
                    .password("aaaaaa1!")
                    .email("okt0517@gmail.com")
                    .name(name)
                    .termsOfExam(true).termsOfInfo(true)
                    .termsOfSelect(false).build();

            UserSignupResultVO userSignupResultVO = UserSignupResultVO.builder()
                    .userId(userId)
                    .name(name)
                    .build();

            String value = objectMapper.writeValueAsString(userSignupDTO);
            String result = objectMapper.writeValueAsString(userSignupResultVO);

            mockMvc.perform(
                    post("/user/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(value)
            ).andExpect(status().isCreated())
                    .andExpect(content().json(result))
                    .andDo(print());

        }

        @Test
        @DisplayName("실패 로직(이미 있는 ID 혹은 Email로 가입을 시도)")
        void fail1() throws Exception{

            UserSignupDTO userSignupDTO = UserSignupDTO.builder()
                    .userId("aa")
                    .password("aaaaaaa!1")
                    .name("aa")
                    .email("aa@naver.com")
                    .termsOfExam(true).termsOfInfo(true)
                    .termsOfSelect(true)
                    .build();

            String value = objectMapper.writeValueAsString(userSignupDTO);

            mockMvc.perform(
                    post("/user/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(value)
            ).andExpect(status().isConflict())
                    .andDo(print());

            UserSignupDTO userSignupDTO1 = UserSignupDTO.builder()
                    .userId("aa")
                    .password("aaaaaa!1")
                    .name("aa")
                    .email("aa@gmail.com")
                    .termsOfExam(true).termsOfInfo(true)
                    .termsOfSelect(true)
                    .build();

            String value1 = objectMapper.writeValueAsString(userSignupDTO1);

            mockMvc.perform(
                            post("/user/signup")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(value1)
                    ).andExpect(status().isConflict())
                    .andDo(print());

        }

        @Test
        @DisplayName("실패 로직(DTO 값 검증에 실패함)")
        void fail2() throws Exception{

            List<String> errorList = new ArrayList<>();
            errorList.add("Pattern.email");
            errorList.add("Pattern.password");

            UserSignupDTO userSignupDTO = UserSignupDTO.builder()
                    .userId("aaa")
                    .password("aa")
                    .name("aa")
                    .email("aa")
                    .termsOfExam(true).termsOfInfo(true)
                    .termsOfSelect(true)
                    .build();

            ErrorVO errorVO = ErrorVO.builder()
                    .errorMessage("validation error")
                    .errors(Collections.singletonList(errorList))
                    .build();

            String value = objectMapper.writeValueAsString(userSignupDTO);
            String error = objectMapper.writeValueAsString(errorVO);

            mockMvc.perform(
                            post("/user/signup")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(value)
                    ).andExpect(status().isBadRequest())
                    .andExpect(content().json(error))
                    .andDo(print());
        }

    }

    @Nested
    @DisplayName("ID 찾기")
    class findUserId {

        @Test
        @DisplayName("성공 로직")
        void success() {

        }

        @Test
        @DisplayName("실패 로직(해당 email로 가입된 user 없음)")
        void fail1() throws Exception{
            String wrongEmail = UUID.randomUUID().toString().substring(0, 6) + "@gmail.com";

            mockMvc.perform(
                    get("/user/find/id")
                            .param("email", wrongEmail)
                            .param("name", name)
            ).andExpect(status().isOk())
                    .andExpect(jsonPath("$").doesNotExist())
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 로직(해당 email로 가입된 이름과 불일치)")
        void fail2() throws Exception{

            mockMvc.perform(
                            get("/user/find/id")
                                    .param("email", email)
                                    .param("name", name+"aa")
                    ).andExpect(status().isBadRequest())
                    .andExpect(content().string("This name not match for name to user"))
                    .andDo(print());

        }

        @Test
        @DisplayName("실패 로직(검증에서 통과하지 못함)")
        void fail3() throws Exception{

            List<String> errorList = new ArrayList<>();
            errorList.add("Pattern.email");
            errorList.add("NotBlank.name");

            ErrorVO errorVO = ErrorVO.builder()
                    .errorMessage("validation error")
                    .errors(Collections.singletonList(errorList))
                    .build();

            String result = objectMapper.writeValueAsString(errorVO);

            mockMvc.perform(
                            get("/user/find/id")
                                    .param("email", "wrongEmail")
                                    .param("name", "   ")
                    ).andExpect(status().isBadRequest())
                    .andExpect(content().json(result))
                    .andDo(print());

        }

    }
}