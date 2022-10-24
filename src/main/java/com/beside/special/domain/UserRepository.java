package com.beside.special.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByAuthProviderAndSubject(AuthProvider authProvider, String subject);
}
