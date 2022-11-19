package com.beside.special.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPlace_IdAndAndCreatedAtAfterOrderByCreatedAt(String placeId, LocalDateTime createAt, Pageable pageable);
}
