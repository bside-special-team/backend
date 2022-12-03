package com.beside.special.controller;

import com.beside.special.domain.AuthUser;
import com.beside.special.domain.Comment;
import com.beside.special.domain.dto.UserDto;
import com.beside.special.service.CommentService;
import com.beside.special.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Comment", description = "댓글")
@RequestMapping("/api/v1/comments")
@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("places/{placeId}")
    public CommentResponses getByPlace(@PathVariable("placeId") String placeId,
                                       @RequestParam(required = false) Long lastTimestamp,
                                       int limit) {
        LocalDateTime datetime = getDatetime(lastTimestamp);
        List<Comment> comments = commentService.findByPlaceIdAndDateTime(placeId, datetime, limit);
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

    @Operation(summary = "코멘트 등록", responses = {
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping
    public ResponseEntity<GainPointResponse<CommentResponse>> register(@Parameter(hidden = true) @AuthUser UserDto userDto,
                                                                       @Valid @RequestBody CreateCommentRequest request) {
        GainPointResponse<Comment> gainPointResponse = commentService.create(userDto.getUserId(), request);
        CommentResponse commentResponse = CommentResponse.from(gainPointResponse.getResponse());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new GainPointResponse(commentResponse, gainPointResponse.getPointResult())
        );
    }

    @Operation(summary = "코멘트 수정", responses = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "403", description = "Comment 작성자 아님"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 Comment"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PatchMapping
    public ResponseEntity<Comment> update(@Parameter(hidden = true) @AuthUser UserDto userDto,
                                          @Valid @RequestBody UpdateCommentRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                commentService.update(userDto.getUserId(), request));
    }

    @Operation(summary = "코멘트 삭제", responses = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "Comment 작성자 아님"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 Comment"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @DeleteMapping
    public ResponseEntity<?> delete(@Parameter(hidden = true) @AuthUser UserDto userDto,
                                    @Valid @RequestBody String commentId) {
        commentService.delete(userDto.getUserId(), commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
