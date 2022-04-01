package com.xinwendewen.flexbox;


public interface MeasureRequest {
    boolean isTight();

    boolean isUnconstrainted();

    int intentSize();

    int getMeasureSpec(); // TODO: 2022/4/1 remove measureSpec
}
