package com.webproject.chatservice.service;

import com.google.gson.JsonObject;
import com.webproject.chatservice.config.JwtTokenProvider;
import com.webproject.chatservice.dto.UserLoginRequestDto;
import com.webproject.chatservice.dto.UserProfileRequestDto;
import com.webproject.chatservice.dto.UserResponseDto;
import com.webproject.chatservice.kakao.KakaoOAuth2;
import com.webproject.chatservice.kakao.KakaoUserInfo;
import com.webproject.chatservice.models.User;
import com.webproject.chatservice.models.UserDetailsImpl;
import com.webproject.chatservice.models.UserRole;
import com.webproject.chatservice.repository.UserRepository;
import com.webproject.chatservice.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoOAuth2 kakaoOAuth2;
    private final JwtTokenProvider jwtTokenProvider;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";
    private final MailUtil mailUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, KakaoOAuth2 kakaoOAuth2,  JwtTokenProvider jwtTokenProvider, MailUtil mailUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kakaoOAuth2 = kakaoOAuth2;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mailUtil = mailUtil;
    }

    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("찾는 유저가 없습니다")
        );
        return user;
    }

    public Long registerUser(User user){
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        userRepository.save(user);
        return user.getId();
    }

    public void signupValidCheck(String Email){
        if (userRepository.findByEmail(Email).isPresent()) {
            throw new IllegalArgumentException("해당 이메일은 이미 가입된 회원이 있습니다.");
        }
    }

    public User loginValidCheck(UserLoginRequestDto userLoginRequestDto){
        User user = userRepository.findByEmail(userLoginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return user;
    }

    public int findPasswordByEamil(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        try {
            return mailUtil.sendMail(user);
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public Long updateUserPassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        user.setPassword(passwordEncoder.encode(password));
        return user.getId();
    }

    public JsonObject kakaoLogin(String accessToken) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(accessToken);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        // 카카오 정보로 회원가입
        if (kakaoUser == null) {
            // 카카오 이메일과 동일한 이메일을 가진 회원이 있는지 확인
            User sameEmailUser = null;

            if (email != null) {
                sameEmailUser = userRepository.findByEmail(email).orElse(null);
            }
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 카카오 이메일과 동일한 이메일 회원이 있는 경우
                // 카카오 Id 를 회원정보에 저장
                kakaoUser.setKakaoId(kakaoId);
                userRepository.save(kakaoUser);
            } else {
                // 카카오 정보로 회원가입
                // username = 카카오 nickname
                String username = nickname;
                // password = 카카오 Id + ADMIN TOKEN
                String password = kakaoId + ADMIN_TOKEN;
                // 패스워드 인코딩
                String encodedPassword = passwordEncoder.encode(password);
                // ROLE = 사용자
                UserRole role = UserRole.USER;

                if (email != null) {
                    kakaoUser = new User(username, encodedPassword, email, role, kakaoId);
                } else {
                    kakaoUser = new User(username, encodedPassword, role, kakaoId);
                }
                userRepository.save(kakaoUser);
            }
        }

        // 스프링 시큐리티 통해 로그인 처리
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("token", jwtTokenProvider.createToken(userDetails.getUser().getId()));
        return jsonObj;
    }

    public User myProfileUpdate(Long id, UserProfileRequestDto userProfileRequestDto) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다")
        );
        if (userProfileRequestDto.getUsername() !=null) {
            user.updateUsername(userProfileRequestDto);
        }
        if (userProfileRequestDto.getEmail() != null) {
            user.updateEmail(userProfileRequestDto);
        }
        if (userProfileRequestDto.getProfileUrl() != null) {
            user.updateProfileUrl(userProfileRequestDto);
        }
        userRepository.save(user);
        return user;
    }

    public List<UserResponseDto> getUserList() {
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for (User user : userRepository.findAllByOrderByEmail()) {
            UserResponseDto userResponseDto = new UserResponseDto(user.getEmail(), user.getUsername());
            userResponseDtoList.add(userResponseDto);
        }

        return userResponseDtoList;
    }
}
