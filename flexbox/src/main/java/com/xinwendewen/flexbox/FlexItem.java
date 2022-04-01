package com.xinwendewen.flexbox;

public interface FlexItem {
    boolean isGone();
    float getFlexBasisPercent();
    void layout(int left, int top, int right, int bottom);

    void measure(MeasureRequest mainAxisMeasureRequest, int occupiedMainSize,
                 MeasureRequest crossAxisMeasureRequest, int occupiedCrossSize,
                 boolean isMainAxisHorizontal);

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

    AlignSelf getAlignSelf();

    int getMainSize(boolean mainAxisHorizontal);

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
                boolean isMainAxisHorizontal, int leftPadding, int topPadding);

    int clampByMinMaxCrossSize(int crossSize, boolean isMainAxisHorizontal);

    void fixedSizeMeasure(int mainSize, int crossSize, boolean isMainAxisHorizontal);
}
