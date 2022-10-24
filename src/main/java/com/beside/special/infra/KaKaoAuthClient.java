package com.beside.special.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KaKaoAuthClient {

    public String getIdTokenByCode(String code) {
        RestTemplate restTemplate = new RestTemplate();
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("client_id", "");
        params.add("redirect_uri", "http://localhost:8080/login");

        KakaoTokenResponse kakaoTokenResponse = restTemplate.postForObject(
            "https://kauth.kakao.com/oauth/token",
            params,
            KakaoTokenResponse.class
        );
        log.info("idToken is {}", kakaoTokenResponse.getIdToken());
        return kakaoTokenResponse.getIdToken();
    }
}
