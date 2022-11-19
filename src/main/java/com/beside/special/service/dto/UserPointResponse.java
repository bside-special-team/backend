package com.beside.special.service.dto;

import lombok.Getter;

@Getter
public class UserPointResponse {
    private UserResponse user;
    private boolean levelUp;

    public UserPointResponse(UserResponse user, boolean levelUp) {
        this.user = user;
        this.levelUp = levelUp;
    }
}
