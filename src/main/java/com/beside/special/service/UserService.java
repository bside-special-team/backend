package com.beside.special.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.beside.special.domain.AuthProvider;
import com.beside.special.domain.User;
import com.beside.special.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
