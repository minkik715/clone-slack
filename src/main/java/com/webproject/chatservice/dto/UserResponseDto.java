package com.webproject.chatservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String email;
    private String name;

    public UserResponseDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
