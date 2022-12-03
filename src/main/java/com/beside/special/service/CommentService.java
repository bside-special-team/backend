package com.beside.special.service;

import com.beside.special.domain.Comment;
import com.beside.special.domain.CommentRepository;
import com.beside.special.domain.Place;
import com.beside.special.domain.PointAction;
import com.beside.special.domain.User;
import com.beside.special.exception.AuthorizationException;
import com.beside.special.exception.NotFoundException;
import com.beside.special.service.dto.CreateCommentRequest;
import com.beside.special.service.dto.GainPointResponse;
import com.beside.special.service.dto.UpdateCommentRequest;
import com.beside.special.service.dto.UserPointResponse;
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
    private final UserPointCalculator userPointCalculator;

    public CommentService(UserService userService,
                          PlaceService placeService,
                          CommentRepository commentRepository,
                          UserPointCalculator userPointCalculator) {
        this.userService = userService;
        this.placeService = placeService;
        this.commentRepository = commentRepository;
        this.userPointCalculator = userPointCalculator;
    }

    @Transactional
    public GainPointResponse<Comment> create(String userId, CreateCommentRequest request) {
        Place place = placeService.findById(request.getPlaceId());
        User user = userService.findById(userId);
        Comment comment = commentRepository.save(new Comment(request.getComment(), user, place));
        UserPointResponse userPointResponse =
                userPointCalculator.calculatePoint(comment.getUser(), PointAction.CREATE_COMMENT, comment.getId());

        return new GainPointResponse(comment, userPointResponse);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByPlaceIdAndDateTime(String placeId, LocalDateTime lastCreateAt, int limit) {
        Pageable page = PageRequest.of(0, limit);
        return commentRepository.findByPlace_IdAndAndCreatedAtAfterOrderByCreatedAt(placeId, lastCreateAt, page);
    }

    @Transactional
    public Comment update(String userId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글입니다."));

        if (comment.getUser().getId().equals(userId)) {
            throw new AuthorizationException("해당 댓글의 작성자가 아닙니다.");
        }

        comment.setComment(request.getComment());

        return commentRepository.save(comment);
    }

    @Transactional
    public void delete(String userId, String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글입니다."));

        if (comment.getUser().getId().equals(userId)) {
            throw new AuthorizationException("해당 댓글의 작성자가 아닙니다.");
        }

        commentRepository.delete(comment);
    }
}
