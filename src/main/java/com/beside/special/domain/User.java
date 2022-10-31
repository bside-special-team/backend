package com.beside.special.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public User(AuthProvider authProvider, String subject, String email, String nickName) {
        this.authProvider = authProvider;
        this.subject = subject;
        this.email = email;
        this.nickName = nickName;
    }
}
