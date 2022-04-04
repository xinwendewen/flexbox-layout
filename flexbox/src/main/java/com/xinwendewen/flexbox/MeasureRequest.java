package com.xinwendewen.flexbox;

public interface MeasureRequest {
    boolean isTight();

    boolean isUnconstrained();

    int getExpectedSize();
}
