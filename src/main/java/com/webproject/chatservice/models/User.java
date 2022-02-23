package com.webproject.chatservice.models;

import com.webproject.chatservice.dto.UserProfileRequestDto;
import com.webproject.chatservice.dto.UserSignupRequestDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Getter
public class User {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Column
    private Long kakaoId;

    @Column
    private String profileUrl;

    public User(UserSignupRequestDto userSignupRequestDto){
        this.username = userSignupRequestDto.getUsername();
        this.password = userSignupRequestDto.getPassword();
        this.email = userSignupRequestDto.getEmail();
        this.role = UserRole.USER;
        this.kakaoId = null;
        this.profileUrl = null;
    }

    public User(String username, String password, String email, UserRole role, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = UserRole.USER;
        this.kakaoId = kakaoId;
        this.profileUrl = null;
    }

    public User(String username, String password, UserRole role, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.role = UserRole.USER;
        this.kakaoId = kakaoId;
        this.profileUrl = null;
    }

    public User(UserProfileRequestDto userProfileRequestDto) {
        this.username = userProfileRequestDto.getUsername();
        this.email = userProfileRequestDto.getEmail();
        this.profileUrl = userProfileRequestDto.getProfileUrl();
    }

    public void updateUsername(UserProfileRequestDto userProfileRequestDto) {
        this.username = userProfileRequestDto.getUsername();
    }

    public void updateEmail(UserProfileRequestDto userProfileRequestDto) {
        this.email = userProfileRequestDto.getEmail();
    }

    public void updateProfileUrl(UserProfileRequestDto userProfileRequestDto) {
        this.profileUrl = userProfileRequestDto.getProfileUrl();
    }

}
