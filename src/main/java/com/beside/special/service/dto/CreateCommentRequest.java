package com.beside.special.service.dto;

import javax.validation.constraints.NotNull;

public class CreateCommentRequest {
    @NotNull(message = "place는 필수값입니다.")
    private String placeId;

    @NotNull(message = "댓글으 필수값입니다.")
    private String comment;

    public String getPlaceId() {
        return placeId;
    }

    public String getComment() {
        return comment;
    }
}
