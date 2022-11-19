package com.beside.special.service.dto;

import lombok.Getter;

@Getter
public class GainPointResponse<T> {
    private final T response;
    private final UserPointResponse pointResult;

    public GainPointResponse(T response, UserPointResponse pointResult) {
        this.response = response;
        this.pointResult = pointResult;
    }
}
