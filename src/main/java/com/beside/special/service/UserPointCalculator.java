package com.beside.special.service;

import com.beside.special.domain.PointAction;
import com.beside.special.domain.User;
import com.beside.special.domain.UserPointHistory;
import com.beside.special.domain.UserPointHistoryRepository;
import com.beside.special.service.dto.UserPointResponse;
import com.beside.special.service.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserPointCalculator {
    private final UserService userService;
    private final UserPointHistoryRepository userPointHistoryRepository;

    public UserPointCalculator(UserService userService, UserPointHistoryRepository userPointHistoryRepository) {
        this.userService = userService;
        this.userPointHistoryRepository = userPointHistoryRepository;
    }

    public UserPointResponse calculatePoint(User user, PointAction action, String targetId) {
        boolean levelUp = user.addPoint(action.getGainPoint());
        userService.save(user);
        userPointHistoryRepository.save(new UserPointHistory(user, action, targetId));
        return new UserPointResponse(UserResponse.from(user), levelUp);
    }
}
