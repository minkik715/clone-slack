package com.webproject.chatservice.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {

    @NotNull
    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    private String email;

    @NotNull
    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    private String password;

}
