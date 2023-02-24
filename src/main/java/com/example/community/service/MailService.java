package com.example.community.service;

import com.example.community.model.DAO.user.UserAuth;
import com.example.community.repository.user.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MailService {

    private final UserAuthRepository userAuthRepository;

    private String createRandomString(){
        return UUID.randomUUID().toString().substring(0, 6);
    }

    /**
     *  toEmail이 있는지를 확인한 후에 횟수를 확인해서
     *  최대 횟수를 넘었다면 IllegalStateException을 발생시킴
     */
    public void sendMail(String toEmail) throws IllegalStateException{
        Optional<UserAuth> optionalUserAuth = userAuthRepository.findByEmail(toEmail);

        if(optionalUserAuth.isPresent()){
            UserAuth userAuth = optionalUserAuth.get();

            Integer number = userAuth.getNumber();

            if(number >= 3) {
                Date date = getDate();

                if(userAuth.getUpdatedDate().after(date) || userAuth.getUpdatedDate().equals(date)){
                    throw new IllegalStateException("This email is maximum request to today");
                }

                String randomString = createRandomString();
                userAuth.update(randomString, 1);

                userAuthRepository.save(userAuth);
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

    /**
     *  오늘 00시의 Date를 구함
     */
    private static Date getDate() {
        Date now = new Date();

        Jsr310JpaConverters.LocalDateConverter converter = new Jsr310JpaConverters.LocalDateConverter();

        LocalDate localDate = converter.convertToEntityAttribute(now);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        return converter.convertToDatabaseColumn(localDateTime.toLocalDate());
    }
}
