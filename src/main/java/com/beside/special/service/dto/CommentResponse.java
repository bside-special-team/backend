package com.beside.special.service.dto;

import com.beside.special.domain.Comment;
import com.beside.special.domain.Place;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private String id;
    private String comment;
    private UserResponse user;
    private String placeId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;

    private Place place;

    public CommentResponse(String id, String comment, UserResponse user, String placeId,
                           LocalDateTime createdAt, LocalDateTime lastModifiedAt, Place place) {
        this.id = id;
        this.comment = comment;
        this.user = user;
        this.placeId = placeId;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.place = place;
    }

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getComment(),
            UserResponse.from(comment.getUser()),
            comment.getPlace().getId(),
            comment.getCreatedAt(),
            comment.getLastModifiedAt(),
            comment.getPlace()
        );
    }
}
