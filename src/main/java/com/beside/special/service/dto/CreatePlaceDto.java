package com.beside.special.service.dto;

import com.beside.special.domain.Coordinate;
import com.beside.special.domain.Season;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.List;

@Schema(name = "플레이스 생성 요청")
@Builder
@Getter
public class CreatePlaceDto {
    @Schema(description = "카테고리 코드", required = true, example = "FOOD")
    @NotNull
    private String categoryCode;

    @Schema(description = "좌표(위/경도)", required = true)
    @NotNull
    private Coordinate coordinate;

    @Schema(description = "명소명", required = true, example = "우리동네", maxLength = 10)
    @Size(max = 10, message = "명소명은 최대 10자 입니다.")
    private String name;

//    @Schema(description = "이미지(아직미구현)", required = true)
//    private List<MultipartFile> images;

    @Schema(description = "명소 소개", required = true, example = "정말 좋아요!")
    @NotNull
    @Size(max = 300)
    private String description;

    @Schema(description = "방문하기 좋은 시간(시작)", example = "10:00", pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime bestStartTime;

    @Schema(description = "방문하기 좋은 시간(끝)", example = "23:00", pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime bestEndTime;

    @Schema(description = "해시태그")
    private List<String> hashTags;

    @Schema(description = "방문하기 좋은 계절", example = "SPRING")
    private Season season;

    @Schema(description = "방문 횟수", example = "10")
    private Integer visitCount;
}
