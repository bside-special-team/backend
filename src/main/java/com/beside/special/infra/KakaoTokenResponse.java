package com.beside.special.infra;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoTokenResponse {
    @JsonAlias("id_token")
    private String idToken;

    public KakaoTokenResponse(String idToken) {
        this.idToken = idToken;
    }
}
