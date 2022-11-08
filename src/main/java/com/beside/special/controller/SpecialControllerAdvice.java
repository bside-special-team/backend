package com.beside.special.controller;

import com.beside.special.config.web.ResponseError;
import com.beside.special.exception.AuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class SpecialControllerAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String message = ex.getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining("\n"));
        return ResponseEntity.badRequest().body(ResponseError.of(message));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ResponseError> unAuthorizationException(AuthorizationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseError.of(exception.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleException(Exception exception) {
        log.error("unknown exception throws {}", exception.getMessage(), exception);

        return ResponseEntity.internalServerError().body(ResponseError.of(exception.getMessage()));
    }
}
