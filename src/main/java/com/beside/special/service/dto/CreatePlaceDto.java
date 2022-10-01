package com.beside.special.service.dto;

import com.beside.special.domain.Coordinate;
import com.beside.special.domain.Season;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.List;

@Getter
public class CreatePlaceDto {
    @NotNull
    private String categoryCode;

    @NotNull
    private Coordinate coordinate;

    @Size(max = 10, message = "명소명은 최대 10자 입니다.")
    private String name;

    private List<MultipartFile> images;

    @NotNull
    @Size(max = 300)
    private String description;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime bestStartTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime bestEndTime;

    private List<String> hashTags;

    private Season season;

    private Integer visitCount;
}
