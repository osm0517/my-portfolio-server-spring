package com.example.community.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class Consistency {

    /**
     * 이메일의 정합성이 안깨지는 규칙
     * 1. @이 있는가?
     * 2. @를 기준으로 앞에 글자가 1글자 이상인가?
     * 3. ###@###.### 형태를 지키는가?
     * **/
    public boolean emailConsistency(String inputEmail){
        try {
//            @가 포함이 됐는지와 .이 포함이 됐는지
            if (!inputEmail.contains("@") || !inputEmail.contains(".")) return false;

            String[] stringList = inputEmail.split("@");
            log.debug(Arrays.toString(stringList));
//            id 부분이 1글자 이상인가
            return stringList[0].length() >= 1;

        }catch (IndexOutOfBoundsException e){
            log.debug(e.getMessage());
            return false;
        }
    }
}
