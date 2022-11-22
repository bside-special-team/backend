package com.beside.special.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Document
public class User {
    private static final int INIT_POINT = 0;

    @Id
    private String id;

    private AuthProvider authProvider;

    private String subject;

    private String email;

    private String nickName;

    private Set<VisitInfo> visitInfos;

    private Set<String> recPlaces;

    private UserLevel userLevel;

    private int point;

    public User(AuthProvider authProvider, String subject, String email, String nickName) {
        this.authProvider = authProvider;
        this.subject = subject;
        this.email = email;
        this.nickName = nickName;
        this.visitInfos = new LinkedHashSet<>();
        this.recPlaces = new LinkedHashSet<>();
        this.userLevel = UserLevel.LEVEL_ONE;
        this.point = INIT_POINT;
    }

    public void update(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return id;
    }

    public String getLabel() {
        return userLevel.getLabel();
    }

    public boolean addPoint(int point) {
        this.point += point;
        UserLevel level = UserLevel.findByPoint(this.point);
        if (this.userLevel != level) {
            this.userLevel = level;
            return true;
        }
        return false;
    }
}
