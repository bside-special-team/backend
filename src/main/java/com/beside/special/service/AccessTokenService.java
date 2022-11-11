package com.beside.special.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.beside.special.config.SpecialJWTConfiguration;
import com.beside.special.domain.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccessTokenService {
    private final SpecialJWTConfiguration specialJWTConfiguration;

    public AccessTokenService(SpecialJWTConfiguration specialJWTConfiguration) {
        this.specialJWTConfiguration = specialJWTConfiguration;
    }
    public String generate(User user) {
        return JWT.create()
            .withClaim("provider", user.getAuthProvider().name())
            .withClaim("userId", user.getId())
            .withClaim("email", user.getEmail())
            .withClaim("nickName", user.getNickName())
            .withExpiresAt(new Date(System.currentTimeMillis() + (1800 * 1000)))
            .sign(Algorithm.HMAC256(specialJWTConfiguration.getSecret()));
    }
}
