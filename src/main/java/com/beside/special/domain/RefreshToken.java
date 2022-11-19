package com.beside.special.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Document
public class RefreshToken extends BaseEntity {

    private static int ONE_WEEK_SECOND = 604800000;
    @Id
    private String id;

    private String accessToken;

    private int expiresIn;

    public RefreshToken(String accessToken) {
        this.accessToken = accessToken;
        this.expiresIn = ONE_WEEK_SECOND;
    }

    public boolean verify(String accessToken) {
        return Objects.equals(this.accessToken, accessToken) &&
            getCreatedAt().plusSeconds(expiresIn).isAfter(LocalDateTime.now());
    }
}
