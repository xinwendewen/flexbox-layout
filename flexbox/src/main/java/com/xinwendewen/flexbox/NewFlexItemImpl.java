package com.xinwendewen.flexbox;

import static com.google.android.flexbox.FlexItem.FLEX_BASIS_PERCENT_DEFAULT;

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
        return flexItem.getHeight() == ViewGroup.LayoutParams.MATCH_PARENT;
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
}
