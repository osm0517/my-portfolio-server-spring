package com.example.community.config;

import com.example.community.utils.jwt.JwtAuthenticationFilter;
import com.example.community.utils.jwt.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig{


    private final JwtConfig jwtConfig;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                    .antMatchers("/user/login", "user/signup", "/swagger-ui.html").permitAll()
//                    .antMatchers("/user/").hasRole("USER")
//                    .anyRequest().authenticated()
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
//                    .loginPage("/user/login")
//                    .loginProcessingUrl("/user/login")
//                    .permitAll()
//                    .and()
                    .disable()
                .logout()
                    .permitAll()
                    .and()
                .csrf()
                    .disable()
                .sessionManagement() //(4)
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .cors()
                    .configurationSource(corsConfigurationSource())
                    .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtConfig),UsernamePasswordAuthenticationFilter.class)



        ;
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //????????? ??????
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        //????????? ??????
        configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
        //????????? ??????
        configuration.setAllowedHeaders(Arrays.asList("*"));
        //?????? ????????? ????????? ?????????
        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //pattern??? ????????? url ???????????? ????????? ???????????? ???
        //????????? ???????????? ????????? ???????????? ?????????
        //url??? ?????? ?????? cors ????????? ????????? ????????? url??? ????????? ???
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}