package com.example.community.utils;

import com.example.community.config.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    //메일로 전송할 랜덤한 문자를 생성
    public String createCode(){
        return UUID.randomUUID().toString().substring(0, 6);
    }

    private MimeMessage createMail(String code, String toEmail, String fromEmail) throws Exception {

        MimeMessage message = javaMailSender.createMimeMessage();

        message.setSubject("테스트 이메일입니다.");
        message.setText("이메일 인증코드 : "+code);
        message.setRecipients(Message.RecipientType.TO, toEmail);

        message.setFrom(fromEmail);

        return message;
    }

    public void sendMail(String code, String toEmail, String fromEmail) throws Exception{
        try{
            MimeMessage message = createMail(code, toEmail, fromEmail);
            javaMailSender.send(message);
        }catch (Exception e){
            String error = e.getClass().toString();
            System.out.println(error);
        }
    }

}
