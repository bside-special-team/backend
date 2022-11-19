package com.beside.special.service.dto;

import com.beside.special.domain.User;
import com.beside.special.domain.UserLevel;
import lombok.Getter;

@Getter
public class UserResponse {
    private String userId;
    private String nickName;
    private UserLevel userLevel;
    private String label;
    private int point;

    public UserResponse(String userId, String nickName, UserLevel userLevel, String label, int point) {
        this.userId = userId;
        this.nickName = nickName;
        this.userLevel = userLevel;
        this.label = label;
        this.point = point;
    }

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getNickName(),
            user.getUserLevel(),
            user.getLabel(),
            user.getPoint()
        );
    }
}
