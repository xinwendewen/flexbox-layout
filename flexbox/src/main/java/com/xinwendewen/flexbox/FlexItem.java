package com.xinwendewen.flexbox;

public interface FlexItem {
    boolean isGone();

    void measure(MeasureRequest mainAxisMeasureRequest, int occupiedMainSize,
                 MeasureRequest crossAxisMeasureRequest, int occupiedCrossSize,
                 boolean isMainAxisHorizontal);

    void fixedMainSizeMeasure(int roundedNewMainSize,
                                     MeasureRequest crossAxisMeasureRequest,
                                     int occupiedCrossSize, boolean isMainAxisHorizontal);
    void clampByMinMaxDimensions();

    int getOuterMainSize(boolean isMainAxisHorizontal);

    boolean requireCrossSizeMatchParent(boolean isMainAxisHorizontal);

    float getFlexGrow();

    float getFlexShrink();

    boolean isFlexible();

    int getOuterCrossSize(boolean isMainAxisHorizontal);

    AlignSelf getAlignSelf();

    int getMeasuredMainSize(boolean mainAxisHorizontal);

    int getMinMainSize(boolean isMainAxisHorizontal);

    float getMaxMainSize(boolean isMainAxisHorizontal);

    int getMainAxisMargin(boolean isMainAxisHorizontal);

    int getMainAxisMarginStart(boolean isMainAxisHorizontal);

    int getMainAxisMarginEnd(boolean isMainAxisHorizontal);

    int getCrossAxisMargin(boolean isMainAxisHorizontal);

    int getCrossAxisMarginStart(boolean isMainAxisHorizontal);

    int getCrossAxisMarginEnd(boolean isMainAxisHorizontal);

    int getMeasuredCrossSize(boolean isMainAxisHorizontal);

    void layout(int mainStart, int mainEnd, int crossStart, int crossEnd,
                boolean isMainAxisHorizontal, int leftPadding, int topPadding);

    int getClampedCrossSize(int crossSize, boolean isMainAxisHorizontal);

    void fixedSizeMeasure(int mainSize, int crossSize, boolean isMainAxisHorizontal);
}
