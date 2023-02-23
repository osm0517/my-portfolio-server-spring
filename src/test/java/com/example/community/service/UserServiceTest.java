package com.example.community.service;

import com.example.community.model.DAO.user.User;
import com.example.community.model.DTO.UserLoginDTO;
import com.example.community.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    private final String userPassword = "testPassword";
    private User user = new User("testId", userPassword, "testName", "testEmail");

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeTestClass
    void testInit(){
        userService = new UserService(userRepository, bCryptPasswordEncoder);
    }

    @AfterEach
    void clean(){
        userRepository.delete(user);
    }

    @Nested
    @DisplayName("로컬 로그인")
    class login{
        String encodedPassword = bCryptPasswordEncoder.encode(userPassword);

        @Test
        @DisplayName("로컬 로그인 성공 로직")
        void localSuccess() {
            String matchId = "testId";
            String matchPassword = "testPassword";

            user.changePassword(encodedPassword);

            userRepository.save(user);

            UserLoginDTO userLoginDTO = new UserLoginDTO(
                    matchId, matchPassword
            );

            User loginUser = userService.login(userLoginDTO);

            assertThat(loginUser.getUserId()).isEqualTo(userLoginDTO.getUserId());
        }

        @Test
        @DisplayName("로컬 로그인 실패 로직(비밀번호가 틀림)")
        void localFailPassword() {
            String matchId = "testId";
            String notMatchPassword = "password";

            user.changePassword(encodedPassword);

            userRepository.save(user);

            UserLoginDTO userLoginDTO = new UserLoginDTO(
                    matchId, notMatchPassword
            );

            User loginUser = userService.login(userLoginDTO);

            assertThat(loginUser).isNull();
        }
    }

    @Nested
    @DisplayName("로컬 회원가입")
    class localSignup{

        @Test
        @DisplayName("회원가입 성공 로직")
        void signupSuccess() {
        }

        @Test
        @DisplayName("회원가입 실패 로직")
        void signupFail() {
        }

    }

    @Test
    void delete() {
    }

    @Test
    void change() {
    }

    @Test
    void find() {
    }
}