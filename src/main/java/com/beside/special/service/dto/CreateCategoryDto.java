package com.beside.special.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryDto {
    @Schema(description = "코드", required = true, example = "FOOD")
    @NotNull(message = "code는 필수값입니다.")
    private String code;

    @Schema(description = "이름", required = true, example = "음식")
    @NotNull(message = "name은 필수값입니다.")
    private String name;
}
