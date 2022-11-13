package com.beside.special.domain;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import lombok.Getter;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    public JwkProvider getProvider() {
        if(this == GOOGLE) {
            return new JwkProviderBuilder(getUrl())
                .cached(10, 7, TimeUnit.DAYS)
                .build();
        }
        return new JwkProviderBuilder(issuer)
            .cached(10, 7, TimeUnit.DAYS)
            .build();
    }

    private URL getUrl() {
        try {
            //TODO 변경하기
            return new URI("https://www.googleapis.com/oauth2/v3/certs").toURL();
        } catch (Exception e) {
            throw new RuntimeException("can not create URL");
        }
    }
}
