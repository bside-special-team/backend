package com.beside.special.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "플레이스 방문 인증 요청")
@Getter
@Setter
@AllArgsConstructor
public class VisitPlaceDto {
    @Schema(description = "유저 ID", required = true)
    private String userId;

    @Schema(description = "장소 ID", required = true)
    private String placeId;
}