package com.beside.special.service;

import com.beside.special.domain.Comment;
import com.beside.special.domain.CommentRepository;
import com.beside.special.domain.dto.UserDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MyService {
    private final CommentRepository commentRepository;

    public MyService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentsByUser(UserDto user, LocalDateTime lastCreateAt, int limit) {
        Pageable page = PageRequest.of(0, limit);
        return commentRepository.findByUser_IdAndAndCreatedAtAfterOrderByCreatedAt(user.getUserId(), lastCreateAt, page);
    }
}
