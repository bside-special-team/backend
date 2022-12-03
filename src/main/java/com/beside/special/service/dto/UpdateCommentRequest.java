package com.beside.special.service.dto;

import javax.validation.constraints.NotNull;

public class UpdateCommentRequest {
    @NotNull(message = "comment는 필수값입니다.")
    private String commentId;

    @NotNull(message = "댓글은 필수값입니다.")
    private String comment;

    public String getCommentId() {
        return commentId;
    }

    public String getComment() {
        return comment;
    }
}
