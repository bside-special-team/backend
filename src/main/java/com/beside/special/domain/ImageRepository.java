package com.beside.special.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ImageRepository extends MongoRepository <Image, String> {
    Optional<Image> findByUuid (String uuid);
}
