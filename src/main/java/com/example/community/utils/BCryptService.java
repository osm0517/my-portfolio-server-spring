package com.example.community.utils;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptService {

    //암호화
    public String encodeBcrypt(String planeText, int strength) {
        return new BCryptPasswordEncoder(strength).encode(planeText);
    }

    //해시 값을 하나 더 생성하여서 동일한지 비교
    public boolean matchesBcrypt(String planeText, String hashValue, int strength) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(strength);
        return passwordEncoder.matches(planeText, hashValue);
    }
}
