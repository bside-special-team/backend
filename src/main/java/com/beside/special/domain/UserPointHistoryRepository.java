package com.beside.special.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserPointHistoryRepository extends MongoRepository<UserPointHistory, String> {
}
