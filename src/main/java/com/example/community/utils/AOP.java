package com.example.community.utils;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Date;


@Aspect
@Component
@Slf4j
public class AOP {

    //시간을 측정하기 위한 변수
    private Date time;

    //가독성을 위해서 실행할 주소와 기능을 구분 짓기
    @Pointcut("execution(* com.example.community.controller.*.*(..))")
    private void controllerPoint() {}

    @Before(value = "controllerPoint()")
    public void controllerBefore(JoinPoint joinPoint){
        time = new Date();
    }
    @After(value = "controllerPoint()")
    public void controllerAfter(JoinPoint joinPoint){
        Date date = new Date();
        log.info("==========");
        log.info(joinPoint.toShortString());
        log.info("operation time = {} ms",date.getTime() - time.getTime());
        log.info("==========");
    }

    @AfterThrowing
    public void controllerThrowing(JoinPoint joinPoint){

    }
}
