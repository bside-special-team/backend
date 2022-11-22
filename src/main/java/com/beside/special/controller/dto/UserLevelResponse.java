package com.beside.special.controller.dto;

import com.beside.special.domain.UserLevel;
import lombok.Getter;

@Getter
public class UserLevelResponse {
    private int level;
    private String label;
    private int minPoint;

    public UserLevelResponse(int level, String label, int minPoint) {
        this.level = level;
        this.label = label;
        this.minPoint = minPoint;
    }

    public static UserLevelResponse from(UserLevel userLevel) {
        return new UserLevelResponse(userLevel.getLevel(), userLevel.getLabel(), userLevel.getMinimumPoint());
    }
}
