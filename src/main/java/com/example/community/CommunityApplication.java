package com.example.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//Spring Boot를 통한 개발을 위해서는 반드시 해당 annotation을 추가
@SpringBootApplication
//AOP를 사용하기 위해서는 해당 annotation을 추가
@EnableAspectJAutoProxy
public class CommunityApplication {

	/*
	* db에 모든 테이블을 일단은 하나로 합침
	* 처음에는 정규화를 생각해보았지만
	* 정규화를 진행하지 않고 그 차이를 비교하는 것이
	* 더 좋을 것 같음
	* */
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
