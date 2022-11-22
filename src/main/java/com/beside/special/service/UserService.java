package com.beside.special.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.beside.special.domain.*;
import com.beside.special.domain.dto.UserDto;
import com.beside.special.domain.AuthProvider;
import static com.beside.special.domain.PointAction.ATTENDANCE;
import com.beside.special.domain.User;
import com.beside.special.domain.UserRepository;
import com.beside.special.service.dto.GainPointResponse;
import com.beside.special.service.dto.UserPointResponse;
import com.beside.special.service.dto.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserPointCalculator userPointCalculator;

    public UserService(UserRepository userRepository, UserPointCalculator userPointCalculator) {
        this.userRepository = userRepository;
        this.userPointCalculator = userPointCalculator;
    }

    @Transactional
    public User findOrCreateByProvider(AuthProvider provider, DecodedJWT jwt) {
        return findOrCreateByProvider(provider, jwt.getSubject(), jwt.getClaim("email").asString());
    }

    @Transactional
    public User findOrCreateByProvider(AuthProvider provider, String subject, String email) {
        return userRepository.findByAuthProviderAndSubject(provider, subject)
            .orElseGet(() -> userRepository.save(
                new User(provider, subject, email, null)
            ));
    }

    @Transactional
    public GainPointResponse<UserResponse> findByIdWithAttendance(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(String.format("not found user %s", id)));
        String targetId = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        UserPointResponse userPointResponse = userPointCalculator.calculatePoint(user, ATTENDANCE, targetId);
        return new GainPointResponse(UserResponse.from(user), userPointResponse);
    }

    public User findById(String id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(String.format("not found user %s", id)));
    }

    @Transactional
    public User update(String userId, String nickName) {
        User user = findById(userId);
        user.update(nickName);
        userRepository.save(user);
        return user;
    }
}
