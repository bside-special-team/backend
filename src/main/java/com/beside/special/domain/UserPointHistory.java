package com.beside.special.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserPointHistory {
    @Id
    private String id;
    private User user;
    private PointAction pointAction;
    private String targetId;

    public UserPointHistory(User user, PointAction pointAction, String targetId) {
        this.user = user;
        this.pointAction = pointAction;
        this.targetId = targetId;
    }
}
