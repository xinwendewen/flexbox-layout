package com.xinwendewen.flexbox;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.google.android.flexbox.FlexContainer.NOT_SET;
import static com.google.android.flexbox.FlexItem.FLEX_BASIS_PERCENT_DEFAULT;
import static com.xinwendewen.flexbox.MeasureRequestUtils.generateExactlyMeasureSpec;

import android.view.ViewGroup;

import com.google.android.flexbox.FlexItem;

public abstract class NewFlexItemImpl implements NewFlexItem {
    public int getFlexBasis(MeasureRequest containerMainMeasureRequest, boolean isMainHorizontal) {
        float flexBasisPercent = getFlexBasisPercent();
        if (flexBasisPercent != FLEX_BASIS_PERCENT_DEFAULT && containerMainMeasureRequest.isTight()) {
            return Math.round(containerMainMeasureRequest.intentSize() * flexBasisPercent);
        } else {
            if (isMainHorizontal) {
                return getRequiredWidth();
            }
            return getRequiredHeight();
        }
    }

    public static boolean isFlexItemHeightMatchParent(FlexItem flexItem) {
        return flexItem.getHeight() == MATCH_PARENT;
    }

    @Override
    public void measure(MeasureRequest mainAxisMeasureRequest, int occupiedMainSize,
                        MeasureRequest crossAxisMeasureRequest, int occupiedCrossSize,
                        boolean isMainAxisHorizontal) {
        int itemFlexBasis = getFlexBasis(mainAxisMeasureRequest, isMainAxisHorizontal);
        int mainMeasureSpec = generateMeasureSpec(mainAxisMeasureRequest.getMeasureSpec(),
                occupiedMainSize + mainAxisMargin(isMainAxisHorizontal), itemFlexBasis);
        int intentCrossSize = getIntentCrossSize(isMainAxisHorizontal);
        int crossMeasureSpec = generateMeasureSpec(crossAxisMeasureRequest.getMeasureSpec(),
                occupiedCrossSize + crossAxisMargin(isMainAxisHorizontal), intentCrossSize);
        if (isMainAxisHorizontal) {
            measure(mainMeasureSpec, crossMeasureSpec);
        } else {
            measure(crossMeasureSpec, mainMeasureSpec);
        }
    }

    protected abstract void measure(int widthSpec, int heightSpec);

    @Override
    public void fixedMainSizeMeasure(int roundedNewMainSize,
                                     MeasureRequest crossAxisMeasureRequest,
                                     int occupiedCrossSize, boolean isMainAxisHorizontal) {
        int mainMeasureSpec = generateExactlyMeasureSpec(roundedNewMainSize);
        int intentCrossSize = getIntentCrossSize(isMainAxisHorizontal);
        int containerCrossMeasureSpec = crossAxisMeasureRequest.getMeasureSpec();
        int crossMeasureSpec = generateMeasureSpec(containerCrossMeasureSpec,
                occupiedCrossSize + crossAxisMargin(isMainAxisHorizontal),
                intentCrossSize);
        if (isMainAxisHorizontal) {
            measure(mainMeasureSpec, crossMeasureSpec);
        } else {
            measure(crossMeasureSpec, mainMeasureSpec);
        }
    }

    @Override
    public void clampByMinMaxCrossSize() {
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
            measure(generateExactlyMeasureSpec(width), generateExactlyMeasureSpec(height));
        }
    }

    @Override
    public int getOuterMainSize(boolean isMainAxisHorizontal) {
        return getMeasuredMainSize(isMainAxisHorizontal) + mainAxisMargin(isMainAxisHorizontal);
    }


    @Override
    public boolean isFlexible() {
        return getFlexShrink() != NOT_SET || getFlexGrow() != NOT_SET;
    }

    @Override
    public int getOuterCrossSize(boolean isMainAxisHorizontal) {
        return getMeasureCrossSize(isMainAxisHorizontal) + crossAxisMargin(isMainAxisHorizontal);
    }


    @Override
    public int getMainSize(boolean isMainAxisHorizontal) {
        return getMeasuredMainSize(isMainAxisHorizontal);
    }

    @Override
    public int getCrossSize(boolean isMainAxisHorizontal) {
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

    @Override
    public int clampByMinMaxCrossSize(int crossSize, boolean isMainAxisHorizontal) {
        crossSize = Math.max(crossSize, getMinCrossSize(isMainAxisHorizontal));
        crossSize = Math.min(crossSize, getMaxCrossSize(isMainAxisHorizontal));
        return crossSize;
    }

    @Override
    public void fixedSizeMeasure(int mainSize, int crossSize, boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            measure(generateExactlyMeasureSpec(mainSize), generateExactlyMeasureSpec(crossSize));
        } else {
            measure(generateExactlyMeasureSpec(crossSize), generateExactlyMeasureSpec(mainSize));
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
    public int minMainSize(boolean isMainAxisHorizontal) {
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
    public float maxMainSize(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMaxWidth();
        } else {
            return getMaxHeight();
        }
    }


    private int getMeasureCrossSize(boolean isMainAxisHorizontal) {
        return isMainAxisHorizontal ? getMeasuredHeight() : getMeasuredWidth();
    }

    private int getMeasuredMainSize(boolean isMainAxisHorizontal) {
        return isMainAxisHorizontal ? getMeasuredWidth() : getMeasuredHeight();
    }


    private int getIntentCrossSize(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getRequiredHeight();
        } else {
            return getRequiredWidth();
        }
    }

    protected abstract int getRequiredWidth();
    protected abstract int getRequiredHeight();

    private int generateMeasureSpec(int containerMeasureSpec, int occupied, int expect) {
        return ViewGroup.getChildMeasureSpec(containerMeasureSpec, occupied, expect);
    }

    @Override
    public int mainAxisMargin(boolean isMainAxisHorizontal) {
       return mainAxisMarginStart(isMainAxisHorizontal) + mainAxisMarginEnd(isMainAxisHorizontal);
    }
    @Override
    public int mainAxisMarginStart(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMarginStart();
        }
        return getMarginTop();
    }
    @Override
    public int mainAxisMarginEnd(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMarginEnd();
        }
        return getMarginBottom();
    }


    @Override
    public int crossAxisMargin(boolean isMainAxisHorizontal) {
        return crossAxisMarginStart(isMainAxisHorizontal) +
                crossAxisMarginEnd(isMainAxisHorizontal);
    }

    @Override
    public int crossAxisMarginStart(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMarginTop();
        } else {
            return getMarginLeft();
        }
    }

    @Override
    public int crossAxisMarginEnd(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMarginBottom();
        } else {
            return getMarginRight();
        }
    }

    protected abstract int getMarginBottom();
    protected abstract int getMarginTop();
    protected abstract int getMarginLeft();
    protected abstract int getMarginRight();
}
