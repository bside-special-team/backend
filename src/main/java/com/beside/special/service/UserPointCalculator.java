package com.beside.special.service;

import com.beside.special.domain.PointAction;
import com.beside.special.domain.User;
import com.beside.special.domain.UserPointHistory;
import com.beside.special.domain.UserPointHistoryRepository;
import com.beside.special.domain.UserRepository;
import com.beside.special.service.dto.UserPointResponse;
import com.beside.special.service.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserPointCalculator {
    private final UserRepository userRepository;
    private final UserPointHistoryRepository userPointHistoryRepository;

    public UserPointCalculator(UserRepository userRepository, UserPointHistoryRepository userPointHistoryRepository) {
        this.userRepository = userRepository;
        this.userPointHistoryRepository = userPointHistoryRepository;
    }

    public UserPointResponse calculatePoint(User user, PointAction action, String targetId) {
        if (action == PointAction.ATTENDANCE && isAlreadyAttendance(user, targetId)) {
            return new UserPointResponse(UserResponse.from(user), false);
        }
        boolean levelUp = user.addPoint(action.getGainPoint());
        userRepository.save(user);
        userPointHistoryRepository.save(new UserPointHistory(user, action, targetId));
        return new UserPointResponse(UserResponse.from(user), levelUp);
    }

    private boolean isAlreadyAttendance(User user, String targetId) {
        return userPointHistoryRepository.existsByUser_IdAndTargetIdAndPointAction(
            user.getId(), targetId, PointAction.ATTENDANCE
        );
    }
}
