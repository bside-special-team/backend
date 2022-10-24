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
        return userRepository.findByAuthProviderAndSubject(provider, jwt.getSubject())
            .orElseGet(() -> userRepository.save(
                new User(provider, jwt.getSubject(), jwt.getClaim("email").asString(), null)
            ));
    }
}
