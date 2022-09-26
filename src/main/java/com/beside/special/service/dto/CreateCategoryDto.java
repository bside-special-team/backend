package com.beside.special.service.dto;

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
    @NotNull(message = "code는 필수값입니다.")
    private String code;
    @NotNull(message = "name은 필수값입니다.")
    private String name;
}
