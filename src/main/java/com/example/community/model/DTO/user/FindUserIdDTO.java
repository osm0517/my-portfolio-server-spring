package com.example.community.model.DTO.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindUserIdDTO {

    @NotBlank @NotNull
    //    이메일 정규식 xxx@xxxx.xxx 식이고 . 뒤에는 반드시 3자리
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z]+\\.[a-zA-Z]{3}+$")
    private String email;

    @NotBlank @NotNull
    private String name;
}
