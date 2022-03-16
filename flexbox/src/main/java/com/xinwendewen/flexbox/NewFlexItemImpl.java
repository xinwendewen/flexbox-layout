package com.xinwendewen.flexbox;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.google.android.flexbox.FlexItem.FLEX_BASIS_PERCENT_DEFAULT;
import static com.xinwendewen.flexbox.MeasureRequestUtils.generateExactlyMeasureSpec;

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

    @Override
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
    public int getFlexBasis(int containerMainMeasureRequest, boolean isMainHorizontal) {
        FlexItem flexItem = (FlexItem) view.getLayoutParams();
        float flexBasisPercent = flexItem.getFlexBasisPercent();
        if (flexBasisPercent != FLEX_BASIS_PERCENT_DEFAULT && MeasureRequestUtils.isTight(containerMainMeasureRequest)) {
            return Math.round(MeasureRequestUtils.getMeasureSpecSize(containerMainMeasureRequest) * flexBasisPercent);
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
    public void clampByMinMaxConstraints() {
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
    public int getOuterCrossSize(boolean isMainAxisHorizontal) {
        return getMeasureCrossSize(isMainAxisHorizontal) + crossAxisMargin(isMainAxisHorizontal);
    }

    @Override
    public int getAlignSelf() {
        return getLayoutParams().getAlignSelf();
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

    int mainAxisMargin(boolean isMainAxisHorizontal) {
       return mainAxisMarginStart(isMainAxisHorizontal) + mainAxisMarginEnd(isMainAxisHorizontal);
    }
    int mainAxisMarginStart(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMarginStart();
        }
        return getMarginTop();
    }
    int mainAxisMarginEnd(boolean isMainAxisHorizontal) {
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

    private int crossAxisMargin(boolean isMainAxisHorizontal) {
        return crossAxisMarginStart(isMainAxisHorizontal) +
                crossAxisMarginEnd(isMainAxisHorizontal);
    }

    private int crossAxisMarginStart(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return getMarginTop();
        } else {
            return getMarginLeft();
        }
    }

    private int crossAxisMarginEnd(boolean isMainAxisHorizontal) {
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
