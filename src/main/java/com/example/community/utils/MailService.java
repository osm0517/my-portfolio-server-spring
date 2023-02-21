//package com.example.community.utils;
//
//import jakarta.mail.Message;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.MailSendException;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//@Slf4j
//public class MailService {
//
//    @Autowired
//    JavaMailSender javaMailSender;
//
//    //메일로 전송할 랜덤한 문자를 생성
//    public String createCode(){
//        return UUID.randomUUID().toString().substring(0, 6);
//    }
//
//    private MimeMessage createMail(String code, String toEmail, String fromEmail) throws MessagingException {
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        message.setSubject("테스트 이메일입니다.");
//        message.setText("이메일 인증코드 : "+code);
//        message.setRecipients(Message.RecipientType.TO, toEmail);
//
//        message.setFrom(fromEmail);
//
//        return message;
//    }
//
//    public void sendMail(String code, String toEmail, String fromEmail){
//        try{
//            MimeMessage message = createMail(code, toEmail, fromEmail);
//            javaMailSender.send(message);
//        }catch (MailSendException e){
//            String error = e.getMessage();
//            log.error("mail send error = {}", error);
//        }catch (MessagingException e){
//            String error = e.getMessage();
//
//            log.info("toEmail = {} sendMail Fail", toEmail);
//
//            log.error("make messaging error = {}", error);
//        }
//    }
//
//}
