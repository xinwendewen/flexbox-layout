package com.google.android.flexbox;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import static com.google.android.flexbox.FlexContainer.NOT_SET;
import static com.google.android.flexbox.FlexItem.FLEX_BASIS_PERCENT_DEFAULT;
import static com.xinwendewen.flexbox.AlignSelf.AUTO;
import static com.xinwendewen.flexbox.AlignSelf.CENTER;
import static com.xinwendewen.flexbox.AlignSelf.FLEX_END;
import static com.xinwendewen.flexbox.AlignSelf.FLEX_START;
import static com.xinwendewen.flexbox.AlignSelf.STRETCH;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.MarginLayoutParamsCompat;

import com.xinwendewen.flexbox.BaseFlexItem;
import com.xinwendewen.flexbox.MeasureRequest;

class ViewWrapper extends BaseFlexItem {
    View view;

    @Override
    public boolean isGone() {
        return view.getVisibility() == View.GONE;
    }

    @Override
    public float getFlexBasisPercent() {
        return getLayoutParams().getFlexBasisPercent();
    }

    @Override
    protected int getRequiredWidth() {
        return getLayoutParams().getWidth();
    }

    @Override
    protected int getRequiredHeight() {
        return getLayoutParams().getHeight();
    }

    @Override
    protected int getMarginBottom() {
        return getLayoutParams().getMarginBottom();
    }

    @Override
    protected int getMarginTop() {
        return getLayoutParams().getMarginTop();
    }

    @Override
    protected int getMarginLeft() {
        return getLayoutParams().getMarginLeft();
    }

    @Override
    protected int getMarginRight() {
        return getLayoutParams().getMarginRight();
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
    public void layout(int left, int top, int right, int bottom) {
        view.layout(left, top, right, bottom);
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

    @Override
    protected boolean isFlexBasisPercentSet() {
        return getFlexBasisPercent() != FLEX_BASIS_PERCENT_DEFAULT;
    }

    @Override
    protected void measure(MeasureRequest parentWidthMeasureRequest, int parentOccupiedWidth,
                           int expectedWidth, MeasureRequest parentHeightMeasureRequest,
                           int parentOccupiedHeight, int expectedHeight) {
        int widthMeasureSpec =
                generateMeasureSpec(((MeasureSpecWrapper)parentWidthMeasureRequest).measureSpec,
                        parentOccupiedWidth + getHorizontalMargin(), expectedWidth);
        int heightMeasureSpec =
                generateMeasureSpec(((MeasureSpecWrapper) parentHeightMeasureRequest).measureSpec,
                        parentOccupiedHeight + getVerticalMargin(), expectedHeight);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void fixedWidthMeasure(int width, MeasureRequest parentHeightMeasureRequest,
                                     int parentOccupiedHeight, int expectedHeight) {
        int widthMeasureSpec = generateExactlyMeasureSpec(width);
        int heightMeasureSpec =
                generateMeasureSpec(((MeasureSpecWrapper) parentHeightMeasureRequest).measureSpec,
                        parentOccupiedHeight + getVerticalMargin(), expectedHeight);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void fixedHeightMeasure(int height, MeasureRequest parentWidthMeasureRequest,
                                      int parentOccupiedWidth, int expectedWidth) {
        int widthMeasureSpec =
                generateMeasureSpec(((MeasureSpecWrapper)parentWidthMeasureRequest).measureSpec,
                        parentOccupiedWidth + getHorizontalMargin(), expectedWidth);
        int heightMeasureSpec = generateExactlyMeasureSpec(height);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void fixedSizeMeasure(int width, int height) {
        int widthMeasureSpec = generateExactlyMeasureSpec(width);
        int heightMeasureSpec = generateExactlyMeasureSpec(height);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    private int getVerticalMargin() {
        return getMarginTop() + getMarginBottom();
    }

    private int getHorizontalMargin() {
        return getMarginStart() + getMarginEnd();
    }

    private int generateMeasureSpec(int containerMeasureSpec, int occupied, int expect) {
        return ViewGroup.getChildMeasureSpec(containerMeasureSpec, occupied, expect);
    }

    public static int generateExactlyMeasureSpec(int size) {
        return View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY);
    }

    @Override
    protected boolean isFlexShrinkSet() {
        return getFlexShrink() != NOT_SET;
    }

    @Override
    protected boolean isFlexGrowSet() {
        return getFlexGrow() != NOT_SET;
    }

    @Override
    protected int getMinWidth() {
        return getLayoutParams().getMinWidth();
    }

    @Override
    protected int getMinHeight() {
        return getLayoutParams().getMinHeight();
    }

    @Override
    protected int getMaxWidth() {
        return getLayoutParams().getMaxWidth();
    }

    @Override
    protected int getMaxHeight() {
        return getLayoutParams().getMaxHeight();
    }

    @Override
    public boolean requireCrossSizeMatchParent(boolean isMainAxisHorizontal) {
        int expectCrossSize = isMainAxisHorizontal ? getRequiredHeight() : getRequiredWidth();
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
    public com.xinwendewen.flexbox.AlignSelf getAlignSelf() {
        switch (getLayoutParams().getAlignSelf()) {
            case com.google.android.flexbox.AlignSelf.FLEX_START:
                return FLEX_START;
            case com.google.android.flexbox.AlignSelf.FLEX_END:
                return FLEX_END;
            case com.google.android.flexbox.AlignSelf.CENTER:
                return CENTER;
            case com.google.android.flexbox.AlignSelf.STRETCH:
                return STRETCH;
            default:
                return AUTO;
        }
    }

    private FlexItem getLayoutParams() {
        return (FlexItem) view.getLayoutParams();
    }
}
