package com.beside.special.service;

import com.beside.special.domain.RefreshToken;
import com.beside.special.domain.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RefreshToken generate(String accessToken) {
        refreshTokenRepository.deleteByAccessToken(accessToken);

        return refreshTokenRepository.save(new RefreshToken(accessToken));
    }
}
