package com.xinwendewen.flexbox;

import com.google.android.flexbox.FlexItem;

public interface NewFlexItem {
    boolean isGone();

    int getMeasuredWidth();
    int getMeasuredHeight();
    void measure(MeasureRequest widthMeasureRequest, MeasureRequest heightMeasureRequest);
    void measure(int widthMeasureRequest, int heightMeasureRequest);
    void layout(int left, int top, int right, int bottom);

    FlexItem getLayoutParams();

    int getMeasuredState();

    int getBaseline();

    int getLeft();

    int getTop();

    int getRight();

    int getBottom();

    int getMarginStart();
    int getMarginEnd();
}
