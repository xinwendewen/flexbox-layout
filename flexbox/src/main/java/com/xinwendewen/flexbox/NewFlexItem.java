package com.xinwendewen.flexbox;

import com.google.android.flexbox.FlexItem;

public interface NewFlexItem {
    boolean isGone();

    int getFlexBasis(MeasureRequest containerMainMeasureRequest, boolean isMainHorizontal);
    int getFlexBasis(int containerMainMeasureRequest, boolean isMainHorizontal);

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

    void measure(int containerMainMeasureSpec, int occupiedMainSize, int containerCrossMeasureSpec, int occupiedCrossSize, boolean isMainAxisHorizontal);

    void fixedMainSizeMeasure(ContainerProperties containerProps, int roundedNewMainSize, int mSumCrossSizeBefore);
    void clampByMinMaxConstraints();

    int getOuterMainSize(boolean isMainAxisHorizontal);

    boolean requireCrossSizeMatchParent(boolean isMainAxisHorizontal);

    float getFlexGrow();

    float getFlexShrink();

    boolean isFlexible();

    int getOuterCrossSize(boolean isMainAxisHorizontal);

    int getAlignSelf();

    int getMeasureMainSize(boolean mainAxisHorizontal);

    int minMainSize(boolean isMainAxisHorizontal);

    float maxMainSize(boolean isMainAxisHorizontal);
}
