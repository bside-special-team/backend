package com.beside.special.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
    boolean existsByCode(String code);

    Optional<Category> findCategoryByCode(String code);
}
