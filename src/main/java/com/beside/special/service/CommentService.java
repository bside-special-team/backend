package com.beside.special.service;

import com.beside.special.domain.Comment;
import com.beside.special.domain.CommentRepository;
import com.beside.special.domain.Place;
import com.beside.special.domain.PointAction;
import com.beside.special.domain.User;
import com.beside.special.service.dto.CreateCommentRequest;
import com.beside.special.service.dto.GainPointResponse;
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
        Comment comment = new Comment(request.getComment(), user, place);
        UserPointResponse userPointResponse =
            userPointCalculator.calculatePoint(comment.getUser(), PointAction.CREATE_COMMENT);
        commentRepository.save(comment);

        return new GainPointResponse(comment, userPointResponse);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByPlaceIdAndDateTime(String placeId, LocalDateTime lastCreateAt, int limit) {
        Pageable page = PageRequest.of(0, limit);
        return commentRepository.findByPlace_IdAndAndCreatedAtAfterOrderByCreatedAt(placeId, lastCreateAt, page);
    }
}
