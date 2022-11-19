package com.beside.special.domain.dto;


public class RefreshTokenRequest {
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String toString() {
        return "RefreshTokenRequest{" +
            "accessToken='" + accessToken + '\'' +
            ", refreshToken='" + refreshToken + '\'' +
            '}';
    }
}
