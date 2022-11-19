package com.beside.special.service.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
public class CommentResponses {
    private final List<CommentResponse> comments;
    private final long lastTimestamp;
    private final boolean hasNext;

    public CommentResponses(List<CommentResponse> comments, long lastTimestamp, boolean hasNext) {
        this.comments = comments;
        this.lastTimestamp = lastTimestamp;
        this.hasNext = hasNext;
    }

    public static CommentResponses from(List<CommentResponse> commentResponses, int limit) {
        Long timestamp = commentResponses.stream()
            .map(CommentResponse::getCreatedAt)
            .max(LocalDateTime::compareTo)
            .map(it -> it.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli())
            .orElse(0L);
        return new CommentResponses(commentResponses, timestamp, commentResponses.size() == limit);
    }
}
