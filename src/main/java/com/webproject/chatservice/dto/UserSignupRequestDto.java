package com.webproject.chatservice.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {

    // 한글 영어 - _ 숫자 로 변경 필요
    @NotNull
    @NotBlank(message = "닉네임 입력은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣-_/]{3,10}$", message = "3~10자리의 '-','_', 한글, 알파벳만 사용 가능합니다.")
    private String username;

    @NotNull
    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    private String password;

    @NotNull
    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    private String email;

}
