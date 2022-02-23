package com.webproject.chatservice.controller;

import com.google.gson.JsonObject;
import com.webproject.chatservice.config.JwtTokenProvider;
import com.webproject.chatservice.dto.UserLoginRequestDto;
import com.webproject.chatservice.dto.UserProfileRequestDto;
import com.webproject.chatservice.dto.UserResponseDto;
import com.webproject.chatservice.dto.UserSignupRequestDto;
import com.webproject.chatservice.handler.CustomMessageResponse;
import com.webproject.chatservice.models.User;
import com.webproject.chatservice.models.UserDetailsImpl;
import com.webproject.chatservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //회원 가입
    @PostMapping("/api/user/signup")
    public Object registerUsers(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto) {
        try {
            userService.signupValidCheck(userSignupRequestDto.getEmail());
            User user = new User(userSignupRequestDto);
            return userService.registerUser(user);
        } catch (Exception ignore) {
            CustomMessageResponse customMessageResponse = new CustomMessageResponse(ignore.getMessage(),HttpStatus.BAD_REQUEST.value());
            return customMessageResponse.SendResponse();
        }
    }

    //회원 가입시 이메일 중복체크
    @PostMapping("/api/user/signup/emailCheck")
    public Object validCheckEmail(@RequestBody Map<String, Object> param) {
        try {
            userService.signupValidCheck(param.get("email").toString());
            CustomMessageResponse customMessageResponse = new CustomMessageResponse("사용 가능한 Email입니다.",HttpStatus.OK.value());
            return customMessageResponse.SendResponse();
        } catch (Exception ignore) {
            CustomMessageResponse customMessageResponse = new CustomMessageResponse(ignore.getMessage(),HttpStatus.BAD_REQUEST.value());
            return customMessageResponse.SendResponse();
        }
    }

    //로그인
    @PostMapping("/api/user/login")
    public Object loginUser(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        try {
            User user = userService.loginValidCheck(userLoginRequestDto);
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("token", jwtTokenProvider.createToken(user.getId()));
            return ResponseEntity.ok().body(jsonObj.toString());
        } catch (Exception ignore) {
            CustomMessageResponse customMessageResponse = new CustomMessageResponse(ignore.getMessage(),HttpStatus.BAD_REQUEST.value());
            return customMessageResponse.SendResponse();
        }
    }

    // 카카오 로그인
    // 프론트엔드에서 처리 후 카카오 토큰을 백으로 넘겨 주어 JWT token, username, userid 반환
    @PostMapping("/api/user/kakaoLogin")
    public Object loginUser(@RequestBody Map<String, Object> param) {
        try {
            JsonObject jsonObj = userService.kakaoLogin(param.get("kakaoToken").toString());
            return ResponseEntity.ok().body(jsonObj.toString());
        } catch (Exception ignore) {
            CustomMessageResponse customMessageResponse = new CustomMessageResponse(ignore.getMessage(),HttpStatus.BAD_REQUEST.value());
            return customMessageResponse.SendResponse();
        }
    }

    //비밀번호 찾기
    @PostMapping("/api/user/findPassword")
    public Object findPasswordByEamil(@RequestBody Map<String, Object> param){
        try {
            int CertificationNumber = userService.findPasswordByEamil(param.get("email").toString());
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("CertificationNumber", Integer.toString(CertificationNumber));
            return ResponseEntity.ok().body(jsonObj.toString());
        } catch (Exception ignore) {
            CustomMessageResponse customMessageResponse = new CustomMessageResponse(ignore.getMessage(),HttpStatus.BAD_REQUEST.value());
            return customMessageResponse.SendResponse();
        }
    }

    // 비밀번호 변경
    @PutMapping("/api/user/changePassword")
    public Long updateUserPassword(@RequestBody Map<String, Object> param) {
        String email = param.get("email").toString();
        String password = param.get("password").toString();
        return userService.updateUserPassword(email,password);
    }

    // 마이페이지 프로필 조회
    // token 키 값으로 Header 에 실어주시면 된다!!
    @GetMapping("/api/user/profile")
    public User getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.findById(userDetails.getUser().getId());
    }

    // 마이페이지 프로필 수정
    // username, email, profileurl 만 바꿀 수 있도록 함
    @PutMapping("api/user/profile/{userId}")
    public Object updateMyProfile(@PathVariable Long userId, @Valid @RequestBody UserProfileRequestDto userProfileRequestDto) {
        try {
            return userService.myProfileUpdate(userId, userProfileRequestDto);
        } catch (Exception ignore) {
            CustomMessageResponse customMessageResponse = new CustomMessageResponse(ignore.getMessage(),HttpStatus.BAD_REQUEST.value());
            return customMessageResponse.SendResponse();
        }
    }
    @GetMapping("/api/user")
    public List<UserResponseDto> getUserList(){
        return userService.getUserList();
    }

}