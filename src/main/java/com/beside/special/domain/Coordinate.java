package com.beside.special.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@Getter
public class Coordinate {
    /**
     * 위도
     */
    @Schema(description = "위도", required = true, example = "120.12312312")
    private BigDecimal latitude;
    /**
     * 경도
     */
    @Schema(description = "경도", required = true, example = "20.12312312")
    private BigDecimal longitude;

    private Coordinate() {
    }

    public Coordinate(BigDecimal latitude, BigDecimal longitude) {
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
