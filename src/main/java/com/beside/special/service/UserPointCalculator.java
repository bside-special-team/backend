package com.beside.special.service;

import com.beside.special.domain.PointAction;
import com.beside.special.domain.User;
import com.beside.special.service.dto.UserPointResponse;
import com.beside.special.service.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserPointCalculator {
    private final UserService userService;

    public UserPointCalculator(UserService userService) {
        this.userService = userService;
    }

    public UserPointResponse calculatePoint(User user, PointAction action) {
        boolean levelUp = user.addPoint(action.getGainPoint());
        userService.save(user);
        return new UserPointResponse(UserResponse.from(user), levelUp);
    }
}
