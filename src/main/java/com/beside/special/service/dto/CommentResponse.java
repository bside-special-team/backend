package com.beside.special.service.dto;

import com.beside.special.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private String id;
    private String comment;
    private String userId;
    private String nickName;
    private String placeId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;

    public CommentResponse(String id, String comment, String userId, String nickName, String placeId,
                           LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.id = id;
        this.comment = comment;
        this.userId = userId;
        this.nickName = nickName;
        this.placeId = placeId;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getComment(),
            comment.getUser().getId(),
            comment.getUser().getNickName(),
            comment.getPlace().getId(),
            comment.getCreatedAt(),
            comment.getLastModifiedAt()
        );
    }
}
