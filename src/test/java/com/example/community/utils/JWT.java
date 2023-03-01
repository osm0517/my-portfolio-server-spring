package com.example.community.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class JWT {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void encoder(){
        String encodedChar = bCryptPasswordEncoder.encode("aa");

        Assertions.assertTrue(bCryptPasswordEncoder.matches("aa", encodedChar));
    }
}
