package com.example.community.service;

import com.example.community.model.DAO.user.UserAuth;
import com.example.community.model.DTO.mail.UserAuthMailDTO;
import com.example.community.repository.user.UserAuthRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailServiceTest {

    @Autowired
    MailService mailService;
    @Autowired
    UserAuthRepository userAuthRepository;

    private final String testEmail = "test";
    private final UserAuth userAuth = new UserAuth(testEmail, "random");

    @BeforeTestClass
    void init(){
        mailService = new MailService(userAuthRepository);
    }

    @BeforeEach
    void beforeClean(){
        Optional<UserAuth> optionalUserAuth = userAuthRepository.findByEmail(testEmail);

        if(optionalUserAuth.isPresent()){
            UserAuth userAuth = optionalUserAuth.get();

            userAuthRepository.delete(userAuth);
        }
    }

    @AfterEach
    void afterClean(){
        userAuthRepository.delete(userAuth);
    }

    @Nested
    @DisplayName("메일 전송하고 확인")
    class sendAndCheck{

        @Test
        @DisplayName("성공 로직(1번 이상 시도함)")
        void success1(){
            userAuthRepository.save(userAuth);
            String beforeString = userAuth.getRandomString();

            UserAuth findAuth = userAuthRepository.findByEmail(testEmail)
                    .orElse(null);
            assertNotNull(findAuth);

            assertDoesNotThrow(() -> {
                mailService.sendMail(userAuth.getEmail());
            });

            UserAuth result = userAuthRepository.findByEmail(testEmail).get();
            assertEquals(result.getNumber(), 2);
            assertNotEquals(result.getRandomString(), beforeString);
        }

        @Test
        @DisplayName("성공 로직(최초 시도)")
        void success2(){
            assertDoesNotThrow(() -> {
                mailService.sendMail(userAuth.getEmail());
            });
            UserAuth findAuth = userAuthRepository.findByEmail(userAuth.getEmail())
                    .orElse(null);
            assertNotNull(findAuth);
            assertEquals(findAuth.getNumber(), 1);
        }

        @Test
        @DisplayName("실패 로직(시도 횟수를 초과함)")
        void fail1(){
            userAuthRepository.save(userAuth);

            UserAuth auth = userAuthRepository.findByEmail(userAuth.getEmail())
                    .orElse(null);
            assertNotNull(auth);

            auth.update("update",3);
            userAuthRepository.save(auth);

            Throwable exception = assertThrows(IllegalStateException.class, () -> {
                mailService.sendMail(testEmail);
            });
            assertEquals(exception.getMessage(), "This email is maximum request to today");
        }

    }

}