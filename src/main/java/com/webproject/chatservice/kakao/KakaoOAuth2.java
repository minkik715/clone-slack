package com.webproject.chatservice.kakao;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2 {

    public KakaoUserInfo getUserInfo(String accessToken) {
        // 2. 액세스 토큰 -> 카카오 사용자 정보
        KakaoUserInfo userInfo = getUserInfoByToken(accessToken);

        return userInfo;
    }

    private KakaoUserInfo getUserInfoByToken(String accessToken) {
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        JSONObject body = new JSONObject(response.getBody());
        Long id = body.getLong("id");
        String email = null;
        if(!body.getJSONObject("kakao_account").getBoolean("email_needs_agreement"))
        {
            email = body.getJSONObject("kakao_account").getString("email");
        }
        String nickname = body.getJSONObject("properties").getString("nickname");

        if (email == null)
        {
            return new KakaoUserInfo(id, nickname);
        }

        else{
            return new KakaoUserInfo(id, email, nickname);
        }
    }
}
