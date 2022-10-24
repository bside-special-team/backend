package com.beside.special.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum AuthProvider {
    KAKAO("https://kauth.kakao.com"),
    FACEBOOK("EMPTY"),
    GOOGLE("https://accounts.google.com");

    private final String issuer;

    AuthProvider(String issuer) {
        this.issuer = issuer;
    }

    public static AuthProvider findByIssuer(String issuer) {
        return Arrays.stream(values())
            .filter(provider -> Objects.equals(provider.issuer, issuer))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
