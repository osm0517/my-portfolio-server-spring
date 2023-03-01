package com.example.community.model.DTO.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupDTO {

    @NotNull @NotBlank
    private String userId;

//    1개 이상의 문자열 1개 이상의 숫자 1개 이상의 특수문자 최소 8자 이상 12자 이하
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,12}$")
    @NotNull @NotBlank
    private String password;

    @NotNull @NotBlank
    private String name;

//    이메일 정규식 xxx@xxxx.xxx 식이고 . 뒤에는 반드시 3자리
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z]+\\.[a-zA-Z]{3}+$")
    @NotNull @NotBlank
    private String email;

//    필수 약관
    @AssertTrue
    @NotNull
    private boolean termsOfInfo;

    @AssertTrue
    @NotNull
    private boolean termsOfExam;
//    선택 약관
    @NotNull
    private boolean termsOfSelect;
}
