package com.beside.special.service;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.beside.special.config.SpecialOAuth2ClientProperties;
import com.beside.special.domain.AuthProvider;
import com.beside.special.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthService {
    private final UserService userService;
    private final SpecialOAuth2ClientProperties specialOAuth2ClientProperties;

    public AuthService(UserService userService,
                       SpecialOAuth2ClientProperties specialOAuth2ClientProperties) {
        this.userService = userService;
        this.specialOAuth2ClientProperties = specialOAuth2ClientProperties;
    }

    public String generateAccessToken(String idToken) throws JwkException {
        DecodedJWT jwt = verify(idToken);
        AuthProvider provider = AuthProvider.findByIssuer(jwt.getIssuer());
        User user = userService.findOrCreateByProvider(provider, jwt);

        return JWT.create()
            .withClaim("provider", user.getAuthProvider().name())
            .withClaim("userId", user.getId())
            .withClaim("email", user.getEmail())
            .withClaim("nickName", user.getNickName())
            .withExpiresAt(new Date(System.currentTimeMillis() + (1800 * 1000)))
            //TODO secret 변경
            .sign(Algorithm.HMAC256("qwelrkjasdlfvkjasd"));
    }

    private DecodedJWT verify(String token) throws JwkException {
        DecodedJWT idToken = JWT.decode(token);
        AuthProvider authProvider = AuthProvider.findByIssuer(idToken.getIssuer());
        SpecialOAuth2ClientProperties.SpecialRegistration specialRegistration =
            specialOAuth2ClientProperties.getRegistration().get(authProvider.name().toLowerCase());

        // issuer 검증
        if (!Objects.equals(idToken.getIssuer(), authProvider.getIssuer())) {
            throw new IllegalArgumentException("issuer 불일치");
        }

        // client_id 검증
        if (!idToken.getAudience().contains(specialRegistration.getClientId())) {
            throw new IllegalArgumentException("appKey 불일치");
        }

        // token expiration 검증(?)
        if (idToken.getExpiresAt().before(new Date(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("만료시간 지남");
        }

        //서명 검증
        JwkProvider provider = new JwkProviderBuilder(authProvider.getIssuer())
            .cached(10, 7, TimeUnit.DAYS)
            .build();

        Jwk jwk = provider.get(idToken.getKeyId());
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
        JWTVerifier verifier = JWT.require(algorithm)
            .build();
        return verifier.verify(idToken);
    }
}
