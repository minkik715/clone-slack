package com.webproject.chatservice.dto;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequestDto {

    private Long id;

    @NotNull
    @NotBlank(message = "닉네임 입력은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣-_/]{3,10}$", message = "3~10자리의 '-','_', 한글, 알파벳만 사용 가능합니다.")
    private String username;

    private String email;

    private String profileUrl;

}
