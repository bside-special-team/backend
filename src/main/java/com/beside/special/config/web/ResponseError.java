package com.beside.special.config.web;

import lombok.Getter;

@Getter
public class ResponseError {
    private String message;

    public ResponseError(String message) {
        this.message = message;
    }

    public static ResponseError of(String message) {
        return new ResponseError(message);
    }
}
