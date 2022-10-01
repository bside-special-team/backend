package com.beside.special.domain;

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
    private BigDecimal latitude;
    /**
     * 경도
     */
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
