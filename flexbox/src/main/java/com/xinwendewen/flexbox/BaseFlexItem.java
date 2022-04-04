package com.xinwendewen.flexbox;

public abstract class BaseFlexItem implements FlexItem {
    public int getFlexBasis(MeasureRequest containerMainMeasureRequest, boolean isMainHorizontal) {
        float flexBasisPercent = getFlexBasisPercent();
        if (isFlexBasisPercentSet() && containerMainMeasureRequest.isTight()) {
            return Math.round(containerMainMeasureRequest.getExpectedSize() * flexBasisPercent);
        } else {
            if (isMainHorizontal) {
                return getRequiredWidth();
            }
            return getRequiredHeight();
        }
    }

    protected abstract boolean isFlexBasisPercentSet();

    @Override
    public void measure(MeasureRequest mainAxisMeasureRequest, int occupiedMainSize,
                        MeasureRequest crossAxisMeasureRequest, int occupiedCrossSize,
                        boolean isMainAxisHorizontal) {
        int itemFlexBasis = getFlexBasis(mainAxisMeasureRequest, isMainAxisHorizontal);
        int expectedCrossSize = getRequiredCrossSize(isMainAxisHorizontal);
        if (isMainAxisHorizontal) {
            measure(mainAxisMeasureRequest, occupiedMainSize, itemFlexBasis,
                    crossAxisMeasureRequest, occupiedCrossSize, expectedCrossSize);
        } else {
            measure(crossAxisMeasureRequest, occupiedCrossSize, expectedCrossSize,
                    mainAxisMeasureRequest, occupiedMainSize, itemFlexBasis);
        }
    }

    protected abstract void measure(MeasureRequest parentWidthMeasureRequest,
                                    int parentOccupiedWidth, int expectedWidth,
                                    MeasureRequest parentHeightMeasureRequest,
                                    int parentOccupiedHeight, int expectedHeight);
    protected abstract void fixedWidthMeasure(int width, MeasureRequest parentHeightMeasureRequest,
                                              int parentOccupiedHeight, int expectedHeight);
    protected abstract void fixedHeightMeasure(int height, MeasureRequest parentWidthMeasureRequest,
                                               int parentOccupiedWidth, int expectedWidth);
    protected abstract void fixedSizeMeasure(int width, int height);

    @Override
    public void fixedMainSizeMeasure(int roundedNewMainSize,
                                     MeasureRequest crossAxisMeasureRequest,
                                     int occupiedCrossSize, boolean isMainAxisHorizontal) {
        int expectedCrossSize = getRequiredCrossSize(isMainAxisHorizontal);
        if (isMainAxisHorizontal) {
            fixedWidthMeasure(roundedNewMainSize, crossAxisMeasureRequest, occupiedCrossSize,
                    expectedCrossSize);
        } else {
            fixedHeightMeasure(roundedNewMainSize, crossAxisMeasureRequest, occupiedCrossSize,
                    expectedCrossSize);
        }
    }

    @Override
    public void clampByMinMaxDimensions() {
        boolean violated = false;
        int width = getMeasuredWidth();
        if (width < getMinWidth()) {
            violated = true;
            width = getMinWidth();
        } else if (width > getMaxWidth()) {
            violated = true;
            width = getMaxWidth();
        }

        int height = getMeasuredHeight();
        if (height < getMinHeight()) {
            violated = true;
            height = getMinHeight();
        } else if (height > getMaxHeight()) {
            violated = true;
            height = getMaxWidth();
        }

        if (violated) {
            fixedSizeMeasure(width, height);
        }
    }

    protected abstract float getFlexBasisPercent();

    @Override
    public int getOuterMainSize(boolean isMainAxisHorizontal) {
        return getMeasuredMainSize(isMainAxisHorizontal) + getMainAxisMargin(isMainAxisHorizontal);
    }


    @Override
    public boolean isFlexible() {
        return isFlexShrinkSet() || isFlexGrowSet();
    }

    protected abstract boolean isFlexShrinkSet();
    protected abstract boolean isFlexGrowSet();
    @Override
    public int getOuterCrossSize(boolean isMainAxisHorizontal) {
        return getMeasureCrossSize(isMainAxisHorizontal) + getCrossAxisMargin(isMainAxisHorizontal);
    }


    @Override
    public int getMeasuredMainSize(boolean isMainAxisHorizontal) {
        return isMainAxisHorizontal ? getMeasuredWidth() : getMeasuredHeight();
    }

    @Override
    public int getMeasuredCrossSize(boolean isMainAxisHorizontal) {
        return isMainAxisHorizontal ? getMeasuredHeight() : getMeasuredWidth();
    }

    @Override
    public void layout(int mainStart, int mainEnd, int crossStart, int crossEnd,
                       boolean isMainAxisHorizontal, int leftPadding, int topPadding) {
        int left = leftPadding;
        int top =  topPadding;
        int right = leftPadding;
        int bottom = topPadding;
        if (isMainAxisHorizontal) {
            left += mainStart;
            right += mainEnd;
            top += crossStart;
            bottom += crossEnd;
        } else {
            left += crossStart;
            right += crossEnd;
            top += mainStart;
            bottom += mainEnd;
        }
        layout(left, top, right, bottom);
    }

    protected abstract void layout(int left, int top, int right, int bottom);

    @Override
    public int getClampedCrossSize(int crossSize, boolean isMainAxisHorizontal) {
        crossSize = Math.max(crossSize, getMinCrossSize(isMainAxisHorizontal));
        crossSize = Math.min(crossSize, getMaxCrossSize(isMainAxisHorizontal));
        return crossSize;
    }

    @Override
    public void fixedSizeMeasure(int mainSize, int crossSize, boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            fixedSizeMeasure(mainSize, crossSize);
        } else {
            fixedSizeMeasure(crossSize, mainSize);
        }
    }

    private int getMaxCrossSize(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMaxHeight();
        } else {
            return getMaxWidth();
        }
    }

    private int getMinCrossSize(boolean isMainAxisHorizontal) {
    if (isMainAxisHorizontal) {
            return getMinHeight();
        } else {
            return getMinWidth();
        }
    }

    @Override
    public int getMinMainSize(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMinWidth();
        } else {
            return getMinHeight();
        }
    }

    protected abstract int getMinWidth();

    protected abstract int getMinHeight();

    protected abstract int getMaxWidth();

    protected abstract int getMaxHeight();

    @Override
    public float getMaxMainSize(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMaxWidth();
        } else {
            return getMaxHeight();
        }
    }

    private int getMeasureCrossSize(boolean isMainAxisHorizontal) {
        return isMainAxisHorizontal ? getMeasuredHeight() : getMeasuredWidth();
    }

    protected abstract int getMeasuredWidth();
    protected abstract int getMeasuredHeight();

    private int getRequiredCrossSize(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getRequiredHeight();
        } else {
            return getRequiredWidth();
        }
    }

    protected abstract int getRequiredWidth();
    protected abstract int getRequiredHeight();

    @Override
    public int getMainAxisMargin(boolean isMainAxisHorizontal) {
       return getMainAxisMarginStart(isMainAxisHorizontal) + getMainAxisMarginEnd(isMainAxisHorizontal);
    }
    @Override
    public int getMainAxisMarginStart(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMarginStart();
        }
        return getMarginTop();
    }
    @Override
    public int getMainAxisMarginEnd(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMarginEnd();
        }
        return getMarginBottom();
    }


    @Override
    public int getCrossAxisMargin(boolean isMainAxisHorizontal) {
        return getCrossAxisMarginStart(isMainAxisHorizontal) +
                getCrossAxisMarginEnd(isMainAxisHorizontal);
    }

    @Override
    public int getCrossAxisMarginStart(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMarginTop();
        } else {
            return getMarginLeft();
        }
    }

    @Override
    public int getCrossAxisMarginEnd(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMarginBottom();
        } else {
            return getMarginRight();
        }
    }

    protected abstract int getMarginStart();
    protected abstract int getMarginEnd();
    protected abstract int getMarginBottom();
    protected abstract int getMarginTop();
    protected abstract int getMarginLeft();
    protected abstract int getMarginRight();
}
