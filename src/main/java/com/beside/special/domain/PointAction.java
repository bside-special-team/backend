package com.beside.special.domain;

public enum PointAction {
    ATTENDANCE(5),
    VISIT(50),
    CREATE_COMMENT(10),
    ADVENTURE(1),
    CREATE_PLACE(100);
    private final int gainPoint;

    PointAction(int gainPoint) {
        this.gainPoint = gainPoint;
    }

    public int getGainPoint() {
        return gainPoint;
    }
}
