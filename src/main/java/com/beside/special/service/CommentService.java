package com.beside.special.service;

import com.beside.special.domain.Comment;
import com.beside.special.domain.CommentRepository;
import com.beside.special.domain.Place;
import com.beside.special.domain.User;
import com.beside.special.service.dto.CreateCommentRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final UserService userService;
    private final PlaceService placeService;
    private final CommentRepository commentRepository;

    public CommentService(UserService userService,
                          PlaceService placeService,
                          CommentRepository commentRepository) {
        this.userService = userService;
        this.placeService = placeService;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment create(String userId, CreateCommentRequest request) {
        Place place = placeService.findById(request.getPlaceId());
        User user = userService.findById(userId);

        Comment comment = new Comment(request.getComment(), user, place);
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByPlaceIdAndDateTime(String placeId, LocalDateTime lastCreateAt, int limit) {
        Pageable page = PageRequest.of(0, limit);
        return commentRepository.findByPlace_IdAndAndCreatedAtAfterOrderByCreatedAt(placeId, lastCreateAt, page);
    }
}
