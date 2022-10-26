package com.example.community.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                    .antMatchers("/user/test").permitAll()
//                    .anyRequest().authenticated()
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .loginPage("/logintest")
                    .loginProcessingUrl("/user/loginlogin")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll()
                    .and()
                .csrf()
                    .disable()
                .cors()
                    .configurationSource(corsConfigurationSource())



        ;
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //허용할 출처
        configuration.addAllowedOrigin("*");
        //허용할 헤더
        configuration.addAllowedHeader("*");
        //허용할 요청
        configuration.addAllowedMethod("*");
        //자격 증명을 사용할 것인가
        configuration.setAllowCredentials(false);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //pattern에 들어간 url 경로에서 요청이 들어왔을 때
        //설정을 기반으로 가능한 요청인지 탐색함
        //url에 따라 다른 cors 전략을 시행할 것이면 url을 나누면 됨
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}