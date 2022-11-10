package com.beside.special.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Document
public class User {
    @Id
    private String id;

    private AuthProvider authProvider;

    private String subject;

    private String email;

    private String nickName;

    private Set<VisitInfo> visitInfos;

    private Set<String> recPlaces;

    public User(AuthProvider authProvider, String subject, String email, String nickName) {
        this.authProvider = authProvider;
        this.subject = subject;
        this.email = email;
        this.nickName = nickName;
        this.visitInfos = new LinkedHashSet<>();
        this.recPlaces = new LinkedHashSet<>();
    }
}
