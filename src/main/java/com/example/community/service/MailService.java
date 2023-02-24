package com.example.community.service;

import com.example.community.model.DAO.user.UserAuth;
import com.example.community.repository.user.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private final UserAuthRepository userAuthRepository;

    private String createRandomString(){
        return UUID.randomUUID().toString().substring(6);
    }

    /**
     *  toEmail이 있는지를 확인한 후에 횟수를 확인해서
     *  최대 횟수를 넘었다면 IllegalStateException을 발생시킴
     */
    public void sendMail(String toEmail){
        Optional<UserAuth> optionalUserAuth = userAuthRepository.findByEmail(toEmail);

        if(optionalUserAuth.isPresent()){
            UserAuth userAuth = optionalUserAuth.get();

            Integer number = userAuth.getNumber();

            if(number >= 3) {
                throw new IllegalStateException("This email is maximum request to today");
            }else{
                String randomString = createRandomString();
                userAuth.update(randomString, userAuth.getNumber() + 1);

                userAuthRepository.save(userAuth);
            }
        }else{
            String randomString = createRandomString();
            UserAuth userAuth = new UserAuth(toEmail, randomString);

            userAuthRepository.save(userAuth);
        }
        if(!toEmail.equals("test")){
//                    메일 전송 로직 추가해야 됨
        }
    }
}
