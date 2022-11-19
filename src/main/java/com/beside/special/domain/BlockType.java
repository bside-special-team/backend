package com.beside.special.domain;

public enum BlockType {
    PLACE("플레이스"),
    COMMENT("댓글"),
    USER("사용자");

    private String target;

    BlockType(String target) {
        this.target = target;
    }
}
