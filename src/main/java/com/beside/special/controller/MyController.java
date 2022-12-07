package com.beside.special.controller;

import com.beside.special.domain.AuthUser;
import com.beside.special.domain.Comment;
import com.beside.special.domain.dto.UserDto;
import com.beside.special.service.MyService;
import com.beside.special.service.dto.CommentResponse;
import com.beside.special.service.dto.CommentResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "My", description = "내 활동")
@RequestMapping("/api/v1/my")
@RestController
public class MyController {
    private final MyService myService;

    public MyController(MyService myService) {
        this.myService = myService;
    }

    @Operation(summary = "내 활동 > 내 댓글", responses = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping("/comments")
    public CommentResponses comments(@Parameter(hidden = true) @AuthUser UserDto user,
                                     @RequestParam(required = false) Long lastTimestamp,
                                     int limit) {
        LocalDateTime datetime = getDatetime(lastTimestamp);
        List<Comment> comments = myService.findCommentsByUser(user, datetime, limit);
        List<CommentResponse> commentResponses = comments.stream()
            .map(CommentResponse::from)
            .collect(Collectors.toList());
        return CommentResponses.from(commentResponses, limit);
    }

    private LocalDateTime getDatetime(Long lastTimestamp) {
        if (lastTimestamp != null) {
            return Instant.ofEpochMilli(lastTimestamp).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        }
        return LocalDateTime.of(1999, 10, 25, 0, 0, 0);
    }
}
