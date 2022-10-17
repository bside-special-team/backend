package com.beside.special.service;

import com.auth0.jwk.JwkException;
import com.beside.special.domain.RefreshToken;
import com.beside.special.domain.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public LoginService(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    public TokenResponse login(String idToken) throws JwkException {
        String accessToken = authService.generateAccessToken(idToken);
        RefreshToken refreshToken = refreshTokenService.generate(accessToken);
        return new TokenResponse(accessToken, refreshToken.getId());
    }
}
