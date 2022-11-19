package com.beside.special.service.dto;

import com.beside.special.domain.Coordinate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Schema(name = "플레이스 생성 요청")
@Getter
@Setter
@Builder
public class CreatePlaceDto {
    @Schema(description = "좌표(위/경도)", required = true)
    @NotNull
    private Coordinate coordinate;

    @Schema(description = "플레이스명", required = true, example = "우리동네", maxLength = 10)
    @Size(max = 10, message = "플레이스명은 최대 10자 입니다.")
    private String name;

    @Schema(description = "이미지(UUID) List", required = true)
    private List<String> imageUuids;

    @Schema(description = "해시태그 List")
    private List<String> hashTags;
}
