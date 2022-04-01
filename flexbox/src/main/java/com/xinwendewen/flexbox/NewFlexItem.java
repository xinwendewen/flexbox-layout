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

    void measure(int containerMainMeasureSpec, int occupiedMainSize, int containerCrossMeasureSpec,
                 int occupiedCrossSize, boolean isMainAxisHorizontal);
    void measure(MeasureRequest mainAxisMeasureRequest, int occupiedMainSize,
                 MeasureRequest crossAxisMeasureRequest, int occupiedCrossSize,
                 boolean isMainAxisHorizontal);

    void fixedMainSizeMeasure(ContainerProperties containerProps, int roundedNewMainSize, int mSumCrossSizeBefore);
    void fixedMainSizeMeasure(int roundedNewMainSize,
                                     MeasureRequest crossAxisMeasureRequest,
                                     int occupiedCrossSize, boolean isMainAxisHorizontal);
    void clampByMinMaxCrossSize();

    int getOuterMainSize(boolean isMainAxisHorizontal);

    boolean requireCrossSizeMatchParent(boolean isMainAxisHorizontal);

    float getFlexGrow();

    float getFlexShrink();

    boolean isFlexible();

    int getOuterCrossSize(boolean isMainAxisHorizontal);

    int getAlignSelf();

    int getMainSize(boolean mainAxisHorizontal);

    LayoutPositions generateLayoutPosition(int mainStart, int mainEnd, int crossStart,
                                           int crossEnd, boolean isMainAxisHorizontal);

    int minMainSize(boolean isMainAxisHorizontal);

    float maxMainSize(boolean isMainAxisHorizontal);

    int mainAxisMargin(boolean isMainAxisHorizontal);

    int mainAxisMarginStart(boolean isMainAxisHorizontal);

    int mainAxisMarginEnd(boolean isMainAxisHorizontal);

    int crossAxisMargin(boolean isMainAxisHorizontal);

    int crossAxisMarginStart(boolean isMainAxisHorizontal);

    int crossAxisMarginEnd(boolean isMainAxisHorizontal);

    int getCrossSize(boolean isMainAxisHorizontal);

    void layout(int mainStart, int mainEnd, int crossStart, int crossEnd,
                boolean isMainAxisHorizontal, int leftPadding, int topPadding,
                int parentLeft, int parentTop);

    int clampByMinMaxCrossSize(int crossSize, boolean isMainAxisHorizontal);

    void fixedSizeMeasure(int mainSize, int crossSize, boolean isMainAxisHorizontal);
}
