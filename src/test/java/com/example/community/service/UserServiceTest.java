package com.example.community.service;

import com.example.community.model.DAO.user.User;
import com.example.community.model.DTO.user.*;
import com.example.community.model.VO.UserLoginResultVO;
import com.example.community.model.VO.UserSignupResultVO;
import com.example.community.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    private final String userPassword = "testPassword";
    private final String userId = "testId";
    private final String userName = "testName";
    private final String userEmail = "testEmail";
    private final User user = new User(userId, userPassword, userName, userEmail);

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
    void afterClean(){
        userRepository.delete(user);
    }

    @BeforeEach
    void beforeClean(){
        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if(optionalUser.isPresent()){
            User findUser = optionalUser.get();

            userRepository.delete(findUser);
        }

        Optional<User> optionalTestUser = userRepository.findByUserId("testUserId");

        if(optionalTestUser.isPresent()){
            User testUser = optionalTestUser.get();

            userRepository.delete(testUser);
        }
    }

    @Nested
    @DisplayName("로컬 로그인")
    class login{
        String encodedPassword = bCryptPasswordEncoder.encode(userPassword);
        String matchId = "testId";
        String matchPassword = "testPassword";
        String notMatchPassword = "password";

        @Test
        @DisplayName("로컬 로그인 성공 로직")
        void localSuccess() {
            user.changePassword(encodedPassword);

            userRepository.save(user);

            UserLoginDTO userLoginDTO = new UserLoginDTO(
                    matchId, matchPassword, matchPassword
            );

            assertDoesNotThrow(() -> {
                UserLoginResultVO loginUser = userService.login(userLoginDTO);

                assertEquals(loginUser.getUserId(), userLoginDTO.getUserId());
            });
        }

        @Test
        @DisplayName("로컬 로그인 실패 로직(비밀번호가 틀림)")
        void localFailPassword() {
            user.changePassword(encodedPassword);

            userRepository.save(user);

            UserLoginDTO userLoginDTO = new UserLoginDTO(
                    matchId, notMatchPassword, notMatchPassword
            );

            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.login(userLoginDTO);
            });
            assertEquals(exception.getMessage(), "password not match for user");
        }

        @Test
        @DisplayName("로컬 로그인 실패 로직(비밀번호와 비밀번호 확인이 서로 틀림)")
        void localFailConfirm() {
            user.changePassword(encodedPassword);

            userRepository.save(user);

            UserLoginDTO userLoginDTO = new UserLoginDTO(
                    matchId, matchPassword, notMatchPassword
            );

            assertThrows(IllegalArgumentException.class, () -> {
                userService.login(userLoginDTO);
            });
        }
    }

    @Nested
    @DisplayName("로컬 회원가입")
    class localSignup{
        String userId = "testUserId";
        String userPassword = "testPassword";
        String name = "testName";
        String email = "testEmail";
        boolean info = true;
        boolean exam = true;
        boolean select = false;

        @Test
        @DisplayName("회원가입 성공 로직")
        void signupSuccess() {
            UserSignupDTO userSignupDTO = new UserSignupDTO(
                    userId, userPassword, name, email, info, exam, select
            );

            assertDoesNotThrow(() -> {
                UserSignupResultVO savedUser = userService.signup(userSignupDTO);
                User findUser = userRepository.findByUserId(userId)
                        .orElse(null);

                assertNotNull(findUser);
                assertNotNull(savedUser);
                assertNotEquals(findUser.getPassword(), userPassword);
                assertEquals(savedUser.getUserId(), findUser.getUserId());
            });
        }

        @Test
        @DisplayName("회원가입 실패 로직(필수 값이 빠짐)")
        void signupFail1() {
            assertThrows(DataIntegrityViolationException.class, () -> {
                name = null;

                UserSignupDTO passwordNullDTO = new UserSignupDTO(
                        userId, userPassword, name, email, info, exam, select
                );

                userService.signup(passwordNullDTO);
            });

            assertThrows(IllegalArgumentException.class, () -> {
                userPassword = null;

                UserSignupDTO passwordNullDTO = new UserSignupDTO(
                        userId, userPassword, name, email, info, exam, select
                );

                userService.signup(passwordNullDTO);
            });

            User findUser = userRepository.findByUserId(userId)
                    .orElse(null);

            assertNull(findUser);
        }

        @Test
        @DisplayName("회원가입 실패 로직(필수 약관에 동의하지 않음)")
        void signupFail2() {
            info = false;

            UserSignupDTO userSignupDTO = new UserSignupDTO(
                    userId, userPassword, name, email, info, exam, select
            );

            assertDoesNotThrow(() -> {
                UserSignupResultVO savedUser = userService.signup(userSignupDTO);

                assertNull(savedUser);
            });
        }

        @Test
        @DisplayName("회원가입 실패 로직(UNIQUE 제약 조건을 어김)")
        void signupFail3() {
            userId = "testId";

            UserSignupDTO userSignupDTO = new UserSignupDTO(
                    userId, userPassword, name, email, info, exam, select
            );

            User findUser = userRepository.findByUserId(userId)
                    .orElse(null);
            assertNull(findUser);

            userRepository.save(user);

            User result = userRepository.findByUserId(userId)
                    .orElse(null);
            assertNotNull(result);

            assertThrows(DataIntegrityViolationException.class, () ->
                userService.signup(userSignupDTO)
            );
        }

    }

    @Nested
    @DisplayName("로컬 회원 탈퇴")
    class delete{
        String encodedPassword = bCryptPasswordEncoder.encode(userPassword);
        String notMatchPassword = "password";

        @Test
        @DisplayName("성공 로직")
        void success() {
            user.changePassword(encodedPassword);

            userRepository.save(user);

            User findUser = userRepository.findByUserId(user.getUserId())
                    .orElse(null);
            assertNotNull(findUser);

            UserDeleteDTO userDeleteDTO = new UserDeleteDTO(findUser.getId(), userPassword, userPassword);

            boolean result = userService.delete(userDeleteDTO);

            assertTrue(result);

            User resultCheck = userRepository.findByUserId(user.getUserId())
                    .orElse(null);
            assertNull(resultCheck);
        }

        @Test
        @DisplayName("실패 로직(비밀번호와 비밀번호 확인이 서로 다름)")
        void failConfirm() {
            user.changePassword(encodedPassword);

            userRepository.save(user);

            User findUser = userRepository.findByUserId(user.getUserId())
                    .orElse(null);
            assertNotNull(findUser);

            UserDeleteDTO userDeleteDTO = new UserDeleteDTO(findUser.getId(), userPassword, notMatchPassword);

            assertThrows(IllegalArgumentException.class, () -> {
                userService.delete(userDeleteDTO);
            });
        }

        @Test
        @DisplayName("실패 로직(계정의 비밀번호와 입력 비밀번호가 다름)")
        void failNotMatch() {
            userRepository.save(user);

            User findUser = userRepository.findByUserId(user.getUserId())
                    .orElse(null);
            assertNotNull(findUser);

            UserDeleteDTO userDeleteDTO = new UserDeleteDTO(findUser.getId(), notMatchPassword, notMatchPassword);

            boolean result = userService.delete(userDeleteDTO);

            assertFalse(result);
        }

    }

    @Nested
    @DisplayName("로컬 회원 정보 수정")
    class change{

        String changePassword = "password";
        String changeConfirmPassword = "password";
        String changeName = "name";
        String changeEmail = "email";

        @Test
        @DisplayName("성공 로직")
        void success() {
            userRepository.save(user);

            User findUser = userRepository.findByUserId(user.getUserId())
                    .orElse(null);
            assertNotNull(findUser);

            assertEquals(userPassword, findUser.getPassword());

            assertDoesNotThrow(() -> {
                UserInfoChangeDTO userInfoChangeDTO = new UserInfoChangeDTO(changePassword, changeConfirmPassword, null, null);
                User changeUser = userService.change(findUser.getId(), userInfoChangeDTO);

                boolean matches = bCryptPasswordEncoder.matches(changePassword, changeUser.getPassword());
                assertTrue(matches);

                UserInfoChangeDTO nameEmailChange = new UserInfoChangeDTO(null, null, changeName, changeEmail);
                User finalChange = userService.change(changeUser.getId(), nameEmailChange);

                boolean finalMatches = bCryptPasswordEncoder.matches(changePassword, finalChange.getPassword());
                assertTrue(finalMatches);

                assertEquals(changeName, finalChange.getName());
                assertEquals(changeEmail, finalChange.getEmail());
            });
        }

        @Test
        @DisplayName("password 변경 실패 로직(password와 confirm 불일치)")
        void fail1() {
            userRepository.save(user);

            User findUser = userRepository.findByUserId(user.getUserId())
                    .orElse(null);
            assertNotNull(findUser);

            UserInfoChangeDTO userInfoChangeDTO = new UserInfoChangeDTO(
                    changePassword, changeConfirmPassword + "salt", null, null
            );

            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.change(findUser.getId(), userInfoChangeDTO);
            });
            assertEquals(exception.getMessage(), "password not match for confirm");
        }

        @Test
        @DisplayName("변경 실패 로직(모든 인수 blank)")
        void fail2() {
            userRepository.save(user);

            User findUser = userRepository.findByUserId(user.getUserId())
                    .orElse(null);
            assertNotNull(findUser);

            UserInfoChangeDTO userInfoChangeDTO = new UserInfoChangeDTO(
                    "  ", changeConfirmPassword, "", "      "
            );

            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.change(findUser.getId(), userInfoChangeDTO);
            });
            assertEquals(exception.getMessage(), "all argument must not be blank");
        }

    }

    @Nested
    @DisplayName("ID 찾기")
    class findUserId{

        @Test
        @DisplayName("성공 로직")
        void success() {
            userRepository.save(user);

            User findUser = userRepository.findByUserId(userId)
                    .orElse(null);
            assertNotNull(findUser);

            FindUserIdDTO findUserIdDTO = new FindUserIdDTO(userEmail, userName);

            assertDoesNotThrow(() -> {
                String findUserId = userService.findUserId(findUserIdDTO);

                assertEquals(findUserId, userId);
            });
        }

        @Test
        @DisplayName("실패 로직(email을 틀림)")
        void fail1() {
            String notMatchEmail = "email";

            userRepository.save(user);

            User findUser = userRepository.findByUserId(userId)
                    .orElse(null);
            assertNotNull(findUser);

            FindUserIdDTO findUserIdDTO = new FindUserIdDTO(notMatchEmail, userName);

            assertDoesNotThrow( () -> {
                String findUserId = userService.findUserId(findUserIdDTO);

                assertNull(findUserId);
            });
        }

        @Test
        @DisplayName("실패 로직(이름을 틀림)")
        void fail2() {
            String notMatchName = "name";

            userRepository.save(user);

            User findUser = userRepository.findByUserId(userId)
                    .orElse(null);
            assertNotNull(findUser);

            FindUserIdDTO findUserIdDTO = new FindUserIdDTO(userEmail, notMatchName);

            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.findUserId(findUserIdDTO);
            });
            assertEquals(exception.getMessage(), "This name not match for name to user");
        }

    }

    @Nested
    @DisplayName("Password 찾기")
    class findPassword{

        @Test
        @DisplayName("성공 로직")
        void success() {
            userRepository.save(user);

            User findUser = userRepository.findByUserId(userId)
                    .orElse(null);
            assertNotNull(findUser);

            FindPasswordDTO findPasswordDTO = new FindPasswordDTO(userId, userEmail);

            assertDoesNotThrow(() -> {
                String findPassword = userService.findPassword(findPasswordDTO);

                User result = userRepository.findById(findUser.getId())
                        .orElse(null);
                assertNotNull(result);

                assertEquals(result.getPassword(), findPassword);
            });
        }

        @Test
        @DisplayName("실패 로직(email을 틀림)")
        void fail1() {
            String notMatchEmail = "email";

            userRepository.save(user);

            User findUser = userRepository.findByUserId(userId)
                    .orElse(null);
            assertNotNull(findUser);

            FindPasswordDTO findPasswordDTO = new FindPasswordDTO(userId, notMatchEmail);

            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.findPassword(findPasswordDTO);
            });
            assertEquals(exception.getMessage(), "This email not match for email to user");
        }

        @Test
        @DisplayName("실패 로직(userId를 틀림)")
        void fail2() {
            String notMatchUserId = "id";

            userRepository.save(user);

            User findUser = userRepository.findByUserId(userId)
                    .orElse(null);
            assertNotNull(findUser);

            FindPasswordDTO findPasswordDTO = new FindPasswordDTO(notMatchUserId, userEmail);

            assertThrows(NoSuchElementException.class, () -> {
                userService.findPassword(findPasswordDTO);
            });
        }

    }
}