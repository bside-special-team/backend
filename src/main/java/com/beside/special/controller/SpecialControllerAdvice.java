package com.beside.special.controller;

import com.beside.special.config.web.ResponseError;
import com.beside.special.exception.AuthorizationException;
import com.beside.special.exception.BadRequestException;
import com.beside.special.exception.ForbiddenException;
import com.beside.special.exception.NotFoundException;
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

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseError> badRequestException(AuthorizationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.of(exception.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ResponseError> unAuthorizationException(AuthorizationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseError.of(exception.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseError> forbiddenException(AuthorizationException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseError.of(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseError> notFoundException(AuthorizationException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError.of(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleException(Exception exception) {
        log.error("unknown exception throws {}", exception.getMessage(), exception);

        return ResponseEntity.internalServerError().body(ResponseError.of(exception.getMessage()));
    }
}
