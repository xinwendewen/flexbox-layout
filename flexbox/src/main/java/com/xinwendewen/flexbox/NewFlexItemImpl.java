package com.xinwendewen.flexbox;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.google.android.flexbox.FlexContainer.NOT_SET;
import static com.google.android.flexbox.FlexItem.FLEX_BASIS_PERCENT_DEFAULT;
import static com.xinwendewen.flexbox.MeasureRequestUtils.generateExactlyMeasureSpec;
import static com.xinwendewen.flexbox.MeasureRequestUtils.getMeasureSpecSize;
import static com.xinwendewen.flexbox.MeasureRequestUtils.isTight;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.MarginLayoutParamsCompat;

import com.google.android.flexbox.FlexItem;

public class NewFlexItemImpl implements NewFlexItem {
    private final View view;

    public NewFlexItemImpl(View view) {
        this.view = view;
    }

    public static NewFlexItemImpl wrap(View view) {
        return new NewFlexItemImpl(view);
    }

    @Override
    public boolean isGone() {
        return view.getVisibility() == View.GONE;
    }

    public int getFlexBasis(int containerMainMeasureRequest, boolean isMainHorizontal) {
        FlexItem flexItem = (FlexItem) view.getLayoutParams();
        float flexBasisPercent = flexItem.getFlexBasisPercent();
        if (flexBasisPercent != FLEX_BASIS_PERCENT_DEFAULT && isTight(containerMainMeasureRequest)) {
            return Math.round(getMeasureSpecSize(containerMainMeasureRequest) * flexBasisPercent);
        } else {
            if (isMainHorizontal) {
                return flexItem.getWidth();
            }
            return flexItem.getHeight();
        }
    }
    public int getFlexBasis(MeasureRequest containerMainMeasureRequest, boolean isMainHorizontal) {
        FlexItem flexItem = (FlexItem) view.getLayoutParams();
        float flexBasisPercent = flexItem.getFlexBasisPercent();
        if (flexBasisPercent != FLEX_BASIS_PERCENT_DEFAULT && containerMainMeasureRequest.isTight()) {
            return Math.round(containerMainMeasureRequest.intentSize() * flexBasisPercent);
        } else {
            if (isMainHorizontal) {
                return flexItem.getWidth();
            }
            return flexItem.getHeight();
        }
    }

    @Override
    public int getMeasuredWidth() {
        return view.getMeasuredWidth();
    }

    @Override
    public int getMeasuredHeight() {
        return view.getMeasuredHeight();
    }

    @Override
    public void measure(MeasureRequest widthMeasureRequest, MeasureRequest heightMeasureRequest) {

    }

    @Override
    public void measure(int widthMeasureRequest, int heightMeasureRequest) {
        view.measure(widthMeasureRequest, heightMeasureRequest);
    }

    @Override
    public void layout(int left, int top, int right, int bottom) {
        view.layout(left, top, right, bottom);
    }

    @Override
    public FlexItem getLayoutParams() {
        return (FlexItem) view.getLayoutParams();
    }

    @Override
    public int getMeasuredState() {
        return view.getMeasuredState();
    }

    @Override
    public int getBaseline() {
        return view.getBaseline();
    }

    @Override
    public int getLeft() {
        return view.getLeft();
    }

    @Override
    public int getTop() {
        return view.getTop();
    }

    @Override
    public int getRight() {
        return view.getRight();
    }

    @Override
    public int getBottom() {
        return view.getBottom();
    }

    public static boolean isFlexItemHeightMatchParent(FlexItem flexItem) {
        return flexItem.getHeight() == MATCH_PARENT;
    }

    @Override
    public int getMarginStart() {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)
                view.getLayoutParams();
                return  MarginLayoutParamsCompat.getMarginStart(lp);
    }
    @Override
    public int getMarginEnd() {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)
                view.getLayoutParams();
                return MarginLayoutParamsCompat.getMarginEnd(lp);

    }

    ViewGroup.MarginLayoutParams getMarginLayoutParams() {
        return (ViewGroup.MarginLayoutParams)view.getLayoutParams();
    }

    @Override
    public void measure(int containerMainMeasureSpec, int occupiedMainSize, int containerCrossMeasureSpec, int occupiedCrossSize, boolean isMainAxisHorizontal) {
        int itemFlexBasis = getFlexBasis(containerMainMeasureSpec, isMainAxisHorizontal);
        int mainMeasureSpec = generateMeasureSpec(containerMainMeasureSpec,
                occupiedMainSize + mainAxisMargin(isMainAxisHorizontal), itemFlexBasis);
        int intentCrossSize = getIntentCrossSize(isMainAxisHorizontal);
        int crossMeasureSpec = generateMeasureSpec(containerCrossMeasureSpec,
                occupiedCrossSize + crossAxisMargin(isMainAxisHorizontal), intentCrossSize);
        if (isMainAxisHorizontal) {
            view.measure(mainMeasureSpec, crossMeasureSpec);
        } else {
            view.measure(crossMeasureSpec, mainMeasureSpec);
        }
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
            view.measure(mainMeasureSpec, crossMeasureSpec);
        } else {
            view.measure(crossMeasureSpec, mainMeasureSpec);
        }
    }

    @Override
    public void fixedMainSizeMeasure(ContainerProperties containerProps, int roundedNewMainSize,
                                     int occupiedCrossSize) {
        int mainMeasureSpec = generateExactlyMeasureSpec(roundedNewMainSize);
        int intentCrossSize = getIntentCrossSize(containerProps.isMainAxisHorizontal);
        int containerCrossMeasureSpec = containerProps.getCrossAxisMeasureSpec();
        int crossMeasureSpec = generateMeasureSpec(containerCrossMeasureSpec,
                occupiedCrossSize + crossAxisMargin(containerProps.isMainAxisHorizontal),
                intentCrossSize);
        if (containerProps.isMainAxisHorizontal) {
            view.measure(mainMeasureSpec, crossMeasureSpec);
        } else {
            view.measure(crossMeasureSpec, mainMeasureSpec);
        }
    }

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
            view.measure(mainMeasureSpec, crossMeasureSpec);
        } else {
            view.measure(crossMeasureSpec, mainMeasureSpec);
        }
    }

    @Override
    public void clampByMinMaxCrossSize() {
        boolean violated = false;
        int width = view.getMeasuredWidth();
        if (width < getMinWidth()) {
            violated = true;
            width = getMinWidth();
        } else if (width > getMaxWidth()) {
            violated = true;
            width = getMaxWidth();
        }

        int height = view.getMeasuredHeight();
        if (height < getMinHeight()) {
            violated = true;
            height = getMinHeight();
        } else if (height > getMaxHeight()) {
            violated = true;
            height = getMaxWidth();
        }

        if (violated) {
            view.measure(generateExactlyMeasureSpec(width), generateExactlyMeasureSpec(height));
        }
    }

    @Override
    public int getOuterMainSize(boolean isMainAxisHorizontal) {
        return getMeasuredMainSize(isMainAxisHorizontal) + mainAxisMargin(isMainAxisHorizontal);
    }

    @Override
    public boolean requireCrossSizeMatchParent(boolean isMainAxisHorizontal) {
        int expectCrossSize = isMainAxisHorizontal ? getLayoutParams().getHeight() :
                getLayoutParams().getWidth();
        return expectCrossSize == MATCH_PARENT;
    }

    @Override
    public float getFlexGrow() {
        return getLayoutParams().getFlexGrow();
    }

    @Override
    public float getFlexShrink() {
        return getLayoutParams().getFlexShrink();
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
    public int getAlignSelf() {
        return getLayoutParams().getAlignSelf();
    }

    @Override
    public int getMainSize(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return view.getMeasuredWidth();
        } else {
            return view.getMeasuredHeight();
        }
    }

    @Override
    public int getCrossSize(boolean isMainAxisHorizontal) {
        return isMainAxisHorizontal ? view.getMeasuredHeight() : view.getMeasuredWidth();
    }

    @Override
    public void layout(int mainStart, int mainEnd, int crossStart, int crossEnd,
                       boolean isMainAxisHorizontal, int leftPadding, int topPadding,
                       int parentLeft, int parentTop) {
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
        view.layout(left, top, right, bottom);
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
            view.measure(generateExactlyMeasureSpec(mainSize), generateExactlyMeasureSpec(crossSize));
        } else {
            view.measure(generateExactlyMeasureSpec(crossSize),
                    generateExactlyMeasureSpec(mainSize));
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
    public LayoutPositions generateLayoutPosition(int mainStart, int mainEnd, int crossStart,
                                                  int crossEnd, boolean isMainAxisHorizontal) {
        int left;
        int top;
        int right;
        int bottom;
        if (isMainAxisHorizontal) {
            left = mainStart;
            right = mainEnd;
            top = crossStart;
            bottom = crossEnd;
        } else {
            left = crossStart;
            right = crossEnd;
            top = mainStart;
            bottom = mainEnd;
        }
        return new LayoutPositions(left, top, right, bottom);
    }

    @Override
    public int minMainSize(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getLayoutParams().getMinWidth();
        } else {
            return getLayoutParams().getMinHeight();
        }
    }

    @Override
    public float maxMainSize(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getLayoutParams().getMaxWidth();
        } else {
            return getLayoutParams().getMaxHeight();
        }
    }


    private int getMeasureCrossSize(boolean isMainAxisHorizontal) {
        return isMainAxisHorizontal ? getMeasuredHeight() : getMeasuredWidth();
    }

    private int getMeasuredMainSize(boolean isMainAxisHorizontal) {
        return isMainAxisHorizontal ? getMeasuredWidth() : getMeasuredHeight();
    }

    private int getMaxHeight() {
        return getLayoutParams().getMaxHeight();
    }

    private int getMinHeight() {
        return getLayoutParams().getMinHeight();
    }

    private int getMaxWidth() {
        return getLayoutParams().getMaxWidth();
    }

    private int getMinWidth() {
        return getLayoutParams().getMinWidth();
    }

    private int getIntentCrossSize(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getLayoutParams().getHeight();
        } else {
            return getLayoutParams().getWidth();
        }
    }

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

    private int getMarginBottom() {
        return getMarginLayoutParams().bottomMargin;
    }

    private int getMarginTop() {
        return getMarginLayoutParams().topMargin;
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

    private int getMarginRight() {
        return getMarginLayoutParams().rightMargin;
    }


    private int getMarginLeft() {
        return getMarginLayoutParams().leftMargin;
    }
}
