package com.beside.special.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;

@Getter
public enum UserLevel {
    LEVEL_ONE(1, 0, "외지인"),
    LEVEL_TWO(2, 50, "동네주민"),
    LEVEL_THREE(3, 1000, "동네이장"),
    LEVEL_FOUR(4, 3000, "동네통장"),
    LEVEL_FIVE(5, 6000, "동네군수"),
    ;

    private final int level;
    private final int minimumPoint;
    private final String label;

    UserLevel(int level, int minimumPoint, String label) {
        this.level = level;
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
