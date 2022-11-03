package com.example.community.utils;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class AOP {

    //가독성을 위해서 실행할 주소와 기능을 구분 짓기
    @Pointcut("execution(* com.example.community.controller.BoardController.*(..))")
    private void cut() {}

    @Before(value = "cut()")
    public void test(JoinPoint joinPoint){
        System.out.println("JoinPoint = "+ joinPoint);
        System.out.println("AOP test");
    }
}
