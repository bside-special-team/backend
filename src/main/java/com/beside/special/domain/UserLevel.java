package com.beside.special.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;

@Getter
public enum UserLevel {
    LEVEL_ONE(0, "외지인"),
    LEVEL_TWO(50, "동네주민"),
    LEVEL_THREE(1000, "동네이장"),
    LEVEL_FOUR(3000, "동네군수"),
    LEVEL_FIVE(6000, "동네통장"),
    ;

    private final int minimumPoint;
    private final String label;

    UserLevel(int minimumPoint, String label) {
        this.minimumPoint = minimumPoint;
        this.label = label;
    }

    public static UserLevel findByPoint(int point) {
        return Arrays.stream(UserLevel.values())
            .sorted(Comparator.comparing(UserLevel::getMinimumPoint).reversed())
            .filter(it -> point >= it.minimumPoint)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("포인트 확인 필요 [%d]", point)));
    }
}
