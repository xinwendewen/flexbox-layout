/*
 * Copyright 2016 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.flexbox;

import static com.google.android.flexbox.FlexDirection.COLUMN;
import static com.google.android.flexbox.FlexDirection.ROW;
import static com.google.android.flexbox.FlexDirection.ROW_REVERSE;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.xinwendewen.flexbox.FlexContainerImpl;
import com.xinwendewen.flexbox.FlexLine;
import com.xinwendewen.flexbox.Paddings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * A layout that arranges its children in a way its attributes can be specified like the
 * CSS Flexible Box Layout Module.
 * This class extends the {@link ViewGroup} like other layout classes such as {@link LinearLayout}
 * or {@link RelativeLayout}, the attributes can be specified from a layout XML or from code.
 *
 * The supported attributes that you can use are:
 * <ul>
 * <li>{@code flexDirection}</li>
 * <li>{@code flexWrap}</li>
 * <li>{@code justifyContent}</li>
 * <li>{@code alignItems}</li>
 * <li>{@code alignContent}</li>
 * <li>{@code showDivider}</li>
 * <li>{@code showDividerHorizontal}</li>
 * <li>{@code showDividerVertical}</li>
 * <li>{@code dividerDrawable}</li>
 * <li>{@code dividerDrawableHorizontal}</li>
 * <li>{@code dividerDrawableVertical}</li>
 * <li>{@code maxLine}</li>
 * </ul>
 * for the FlexboxLayout.
 *
 * And for the children of the FlexboxLayout, you can use:
 * <ul>
 * <li>{@code layout_order}</li>
 * <li>{@code layout_flexGrow}</li>
 * <li>{@code layout_flexShrink}</li>
 * <li>{@code layout_flexBasisPercent}</li>
 * <li>{@code layout_alignSelf}</li>
 * <li>{@code layout_minWidth}</li>
 * <li>{@code layout_minHeight}</li>
 * <li>{@code layout_maxWidth}</li>
 * <li>{@code layout_maxHeight}</li>
 * <li>{@code layout_wrapBefore}</li>
 * </ul>
 */
public class FlexboxLayout extends ViewGroup implements FlexContainer {
    com.xinwendewen.flexbox.FlexContainer flexContainer = new FlexContainerImpl();

    /**
     * The current value of the {@link FlexDirection}, the default value is {@link
     * FlexDirection#ROW}.
     *
     * @see FlexDirection
     */
    private int mFlexDirection;

    /**
     * The current value of the {@link FlexWrap}, the default value is {@link FlexWrap#NOWRAP}.
     *
     * @see FlexWrap
     */
    private int mFlexWrap;

    /**
     * The current value of the {@link JustifyContent}, the default value is
     * {@link JustifyContent#FLEX_START}.
     *
     * @see JustifyContent
     */
    private int mJustifyContent;

    /**
     * The current value of the {@link AlignItems}, the default value is
     * {@link AlignItems#FLEX_START}.
     *
     * @see AlignItems
     */
    private int mAlignItems;

    /**
     * The current value of the {@link AlignContent}, the default value is
     * {@link AlignContent#FLEX_START}.
     *
     * @see AlignContent
     */
    private int mAlignContent;

    /**
     * The current value of the maxLine attribute, which specifies the maximum number of flex lines.
     */
    private int mMaxLine = NOT_SET;

    /**
     * The int definition to be used as the arguments for the {@link #setShowDivider(int)},
     * {@link #setShowDividerHorizontal(int)} or {@link #setShowDividerVertical(int)}.
     * One or more of the values (such as
     * {@link #SHOW_DIVIDER_BEGINNING} | {@link #SHOW_DIVIDER_MIDDLE}) can be passed to those set
     * methods.
     */
    @IntDef(flag = true,
            value = {
                    SHOW_DIVIDER_NONE,
                    SHOW_DIVIDER_BEGINNING,
                    SHOW_DIVIDER_MIDDLE,
                    SHOW_DIVIDER_END
            })
    @Retention(RetentionPolicy.SOURCE)
    @SuppressWarnings("WeakerAccess")
    public @interface DividerMode {

    }

    /** Constant to show no dividers */
    public static final int SHOW_DIVIDER_NONE = 0;

    /** Constant to show a divider at the beginning of the flex lines (or flex items). */
    public static final int SHOW_DIVIDER_BEGINNING = 1;

    /** Constant to show dividers between flex lines or flex items. */
    public static final int SHOW_DIVIDER_MIDDLE = 1 << 1;

    /** Constant to show a divider at the end of the flex lines or flex items. */
    public static final int SHOW_DIVIDER_END = 1 << 2;

    /** The drawable to be drawn for the horizontal dividers. */
    @Nullable
    private Drawable mDividerDrawableHorizontal;

    /** The drawable to be drawn for the vertical dividers. */
    @Nullable
    private Drawable mDividerDrawableVertical;

    /**
     * Indicates the divider mode for the {@link #mDividerDrawableHorizontal}. The value needs to
     * be the combination of the value of {@link #SHOW_DIVIDER_NONE},
     * {@link #SHOW_DIVIDER_BEGINNING}, {@link #SHOW_DIVIDER_MIDDLE} and {@link #SHOW_DIVIDER_END}
     */
    private int mShowDividerHorizontal;

    /**
     * Indicates the divider mode for the {@link #mDividerDrawableVertical}. The value needs to
     * be the combination of the value of {@link #SHOW_DIVIDER_NONE},
     * {@link #SHOW_DIVIDER_BEGINNING}, {@link #SHOW_DIVIDER_MIDDLE} and {@link #SHOW_DIVIDER_END}
     */
    private int mShowDividerVertical;

    /** The height of the {@link #mDividerDrawableHorizontal}. */
    private int mDividerHorizontalHeight;

    /** The width of the {@link #mDividerDrawableVertical}. */
    private int mDividerVerticalWidth;

    private List<FlexLine> mFlexLines = new ArrayList<>();

    public FlexboxLayout(Context context) {
        this(context, null);
    }

    public FlexboxLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexboxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.FlexboxLayout, defStyleAttr, 0);
        mFlexDirection = a
                .getInt(R.styleable.FlexboxLayout_flexDirection, ROW);
        mFlexWrap = a.getInt(R.styleable.FlexboxLayout_flexWrap, FlexWrap.NOWRAP);
        mJustifyContent = a
                .getInt(R.styleable.FlexboxLayout_justifyContent, JustifyContent.FLEX_START);
        mAlignItems = a.getInt(R.styleable.FlexboxLayout_alignItems, AlignItems.FLEX_START);
        mAlignContent = a.getInt(R.styleable.FlexboxLayout_alignContent, AlignContent.FLEX_START);
        mMaxLine = a.getInt(R.styleable.FlexboxLayout_maxLine, NOT_SET);
        Drawable drawable = a.getDrawable(R.styleable.FlexboxLayout_dividerDrawable);
        if (drawable != null) {
            setDividerDrawableHorizontal(drawable);
            setDividerDrawableVertical(drawable);
        }
        Drawable drawableHorizontal = a
                .getDrawable(R.styleable.FlexboxLayout_dividerDrawableHorizontal);
        if (drawableHorizontal != null) {
            setDividerDrawableHorizontal(drawableHorizontal);
        }
        Drawable drawableVertical = a
                .getDrawable(R.styleable.FlexboxLayout_dividerDrawableVertical);
        if (drawableVertical != null) {
            setDividerDrawableVertical(drawableVertical);
        }
        int dividerMode = a.getInt(R.styleable.FlexboxLayout_showDivider, SHOW_DIVIDER_NONE);
        if (dividerMode != SHOW_DIVIDER_NONE) {
            mShowDividerVertical = dividerMode;
            mShowDividerHorizontal = dividerMode;
        }
        int dividerModeVertical = a
                .getInt(R.styleable.FlexboxLayout_showDividerVertical, SHOW_DIVIDER_NONE);
        if (dividerModeVertical != SHOW_DIVIDER_NONE) {
            mShowDividerVertical = dividerModeVertical;
        }
        int dividerModeHorizontal = a
                .getInt(R.styleable.FlexboxLayout_showDividerHorizontal, SHOW_DIVIDER_NONE);
        if (dividerModeHorizontal != SHOW_DIVIDER_NONE) {
            mShowDividerHorizontal = dividerModeHorizontal;
        }
        a.recycle();
    }

    MeasureRequestImpl widthMeasureRequest = new MeasureRequestImpl(0);

    MeasureRequestImpl heightMeasureRequest = new MeasureRequestImpl(0);

    Paddings paddings = new Paddings();

    List<ViewHolder> flexItems = new ArrayList<>(20);

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        updatePaddings();
        updateFlexContainerProperties();
        ensureFlexItemCapacity(getChildCount());
        for (int i = 0; i < getChildCount(); i++) {
            flexItems.get(i).view = getChildAt(i);
        }
        flexContainer.setFlexItems(flexItems, getChildCount());
        widthMeasureRequest.measureSpec = widthMeasureSpec;
        heightMeasureRequest.measureSpec = heightMeasureSpec;
        if (isMainAxisDirectionHorizontal()) {
            flexContainer.measure(widthMeasureRequest, heightMeasureRequest);
        } else {
            flexContainer.measure(heightMeasureRequest, widthMeasureRequest);
        }
        mFlexLines = flexContainer.getFlexLines();
        setMeasuredDimensionForFlex(mFlexDirection, widthMeasureSpec, heightMeasureSpec, 0);
    }

    private void ensureFlexItemCapacity(int childCount) {
        int current = flexItems.size();
        if (current < childCount) {
            int diff = childCount - current;
            while (diff > 0) {
                flexItems.add(new ViewHolder());
                diff--;
            }
        }
    }

    private void updateFlexContainerProperties() {
        setInnerFlexDirection(mFlexDirection);
        setInnerFlexWrap(mFlexWrap);
        setInnerJustifyContent(mJustifyContent);
        setInnerAlignContent(mAlignContent);
        setInnerAlignItems(mAlignItems);
    }

    private void updatePaddings() {
        paddings.endPadding = getPaddingEnd();
        paddings.startPadding = getPaddingStart();
        paddings.leftPadding = getPaddingLeft();
        paddings.rightPadding = getPaddingRight();
        paddings.topPadding = getPaddingTop();
        paddings.bottomPadding = getPaddingBottom();
        flexContainer.setPaddings(paddings);
    }

    void setInnerFlexDirection(@FlexDirection int flexDirection) {
        switch (flexDirection) {
            case FlexDirection.ROW:
                flexContainer.setFlexDirection(com.xinwendewen.flexbox.FlexDirection.ROW);
                break;
            case FlexDirection.ROW_REVERSE:
                flexContainer.setFlexDirection(com.xinwendewen.flexbox.FlexDirection.ROW_REVERSE);
                break;
            case FlexDirection.COLUMN:
                flexContainer.setFlexDirection(com.xinwendewen.flexbox.FlexDirection.COLUMN);
                break;
            case FlexDirection.COLUMN_REVERSE:
                flexContainer.setFlexDirection(com.xinwendewen.flexbox.FlexDirection.COLUMN_REVERSE);
                break;
        }
    }

    void setInnerFlexWrap(@FlexWrap int flexWrap) {
        switch (flexWrap) {
            case FlexWrap.WRAP:
                flexContainer.setFlexWrap(com.xinwendewen.flexbox.FlexWrap.WRAP);
                break;
            case FlexWrap.NOWRAP:
                flexContainer.setFlexWrap(com.xinwendewen.flexbox.FlexWrap.NOWRAP);
                break;
            case FlexWrap.WRAP_REVERSE:
                flexContainer.setFlexWrap(com.xinwendewen.flexbox.FlexWrap.WRAP_REVERSE);
                break;
        }
    }

    void setInnerJustifyContent(@JustifyContent int justifyContent) {
        switch (justifyContent) {
            case JustifyContent.FLEX_START:
                flexContainer.setJustifyContent(com.xinwendewen.flexbox.JustifyContent.FLEX_START);
                break;
            case JustifyContent.FLEX_END:
                flexContainer.setJustifyContent(com.xinwendewen.flexbox.JustifyContent.FLEX_END);
                break;
            case JustifyContent.CENTER:
                flexContainer.setJustifyContent(com.xinwendewen.flexbox.JustifyContent.CENTER);
                break;
            case JustifyContent.SPACE_BETWEEN:
                flexContainer.setJustifyContent(com.xinwendewen.flexbox.JustifyContent.SPACE_BETWEEN);
                break;
            case JustifyContent.SPACE_AROUND:
                flexContainer.setJustifyContent(com.xinwendewen.flexbox.JustifyContent.SPACE_AROUND);
                break;
            case JustifyContent.SPACE_EVENLY:
                flexContainer.setJustifyContent(com.xinwendewen.flexbox.JustifyContent.SPACE_EVENLY);
                break;
        }
    }

    void setInnerAlignItems(@AlignItems int alignItems) {
        switch (alignItems) {
            case AlignItems.FLEX_START:
                flexContainer.setAlignItems(com.xinwendewen.flexbox.AlignItems.FLEX_START);
                break;
            case AlignItems.FLEX_END:
                flexContainer.setAlignItems(com.xinwendewen.flexbox.AlignItems.FLEX_END);
                break;
            case AlignItems.CENTER:
                flexContainer.setAlignItems(com.xinwendewen.flexbox.AlignItems.CENTER);
                break;
            case AlignItems.STRETCH:
                flexContainer.setAlignItems(com.xinwendewen.flexbox.AlignItems.STRETCH);
                break;
        }
    }

    void setInnerAlignContent(@AlignContent int alignContent) {
        switch (alignContent) {
            case AlignContent.FLEX_START:
                flexContainer.setAlignContent(com.xinwendewen.flexbox.AlignContent.FLEX_START);
                break;
            case AlignContent.FLEX_END:
                flexContainer.setAlignContent(com.xinwendewen.flexbox.AlignContent.FLEX_END);
                break;
            case AlignContent.CENTER:
                flexContainer.setAlignContent(com.xinwendewen.flexbox.AlignContent.CENTER);
                break;
            case AlignContent.SPACE_BETWEEN:
                flexContainer.setAlignContent(com.xinwendewen.flexbox.AlignContent.SPACE_BETWEEN);
                break;
            case AlignContent.SPACE_AROUND:
                flexContainer.setAlignContent(com.xinwendewen.flexbox.AlignContent.SPACE_AROUND);
                break;
            case AlignContent.STRETCH:
                flexContainer.setAlignContent(com.xinwendewen.flexbox.AlignContent.STRETCH);
                break;
        }
    }

    @Override
    public int getFlexItemCount() {
        return getChildCount();
    }

    @Override
    public View getFlexItemAt(int index) {
        return getChildAt(index);
    }

    /**
     * Returns a View, which is reordered by taking {@link LayoutParams#mOrder} parameters
     * into account.
     *
     * @param index the index of the view
     * @return the reordered view, which {@link LayoutParams@order} is taken into account.
     * If the index is negative or out of bounds of the number of contained views,
     * returns {@code null}.
     */
    public View getReorderedChildAt(int index) {
        return getChildAt(index);
    }

    @Override
    public View getReorderedFlexItemAt(int index) {
        return getReorderedChildAt(index);
    }

    /**
     * Set this FlexboxLayouts' width and height depending on the calculated size of main axis and
     * cross axis.
     *
     * @param flexDirection     the value of the flex direction
     * @param widthMeasureSpec  horizontal space requirements as imposed by the parent
     * @param heightMeasureSpec vertical space requirements as imposed by the parent
     * @param childState        the child state of the View
     * @see #getFlexDirection()
     * @see #setFlexDirection(int)
     */
    private void setMeasuredDimensionForFlex(@FlexDirection int flexDirection, int widthMeasureSpec,
            int heightMeasureSpec, int childState) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int calculatedMaxHeight;
        int calculatedMaxWidth;
        switch (flexDirection) {
            case ROW: // Intentional fall through
            case ROW_REVERSE:
                calculatedMaxHeight = getSumOfCrossSize() + getPaddingTop()
                        + getPaddingBottom();
                calculatedMaxWidth = getLargestMainSize() + getPaddingStart() + getPaddingEnd();
                break;
            case COLUMN: // Intentional fall through
            case FlexDirection.COLUMN_REVERSE:
                calculatedMaxHeight = getLargestMainSize();
                calculatedMaxWidth = getSumOfCrossSize() + getPaddingLeft() + getPaddingRight();
                break;
            default:
                throw new IllegalArgumentException("Invalid flex direction: " + flexDirection);
        }

        int widthSizeAndState;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                if (widthSize < calculatedMaxWidth) {
                    childState = View
                            .combineMeasuredStates(childState, View.MEASURED_STATE_TOO_SMALL);
                }
                widthSizeAndState = View.resolveSizeAndState(widthSize, widthMeasureSpec,
                        childState);
                break;
            case MeasureSpec.AT_MOST: {
                if (widthSize < calculatedMaxWidth) {
                    childState = View
                            .combineMeasuredStates(childState, View.MEASURED_STATE_TOO_SMALL);
                } else {
                    widthSize = calculatedMaxWidth;
                }
                widthSizeAndState = View.resolveSizeAndState(widthSize, widthMeasureSpec,
                        childState);
                break;
            }
            case MeasureSpec.UNSPECIFIED: {
                widthSizeAndState = View
                        .resolveSizeAndState(calculatedMaxWidth, widthMeasureSpec, childState);
                break;
            }
            default:
                throw new IllegalStateException("Unknown width mode is set: " + widthMode);
        }
        int heightSizeAndState;
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                if (heightSize < calculatedMaxHeight) {
                    childState = View.combineMeasuredStates(childState,
                            View.MEASURED_STATE_TOO_SMALL
                                    >> View.MEASURED_HEIGHT_STATE_SHIFT);
                }
                heightSizeAndState = View.resolveSizeAndState(heightSize, heightMeasureSpec,
                        childState);
                break;
            case MeasureSpec.AT_MOST: {
                if (heightSize < calculatedMaxHeight) {
                    childState = View.combineMeasuredStates(childState,
                            View.MEASURED_STATE_TOO_SMALL
                                    >> View.MEASURED_HEIGHT_STATE_SHIFT);
                } else {
                    heightSize = calculatedMaxHeight;
                }
                heightSizeAndState = View.resolveSizeAndState(heightSize, heightMeasureSpec,
                        childState);
                break;
            }
            case MeasureSpec.UNSPECIFIED: {
                heightSizeAndState = View.resolveSizeAndState(calculatedMaxHeight,
                        heightMeasureSpec, childState);
                break;
            }
            default:
                throw new IllegalStateException("Unknown height mode is set: " + heightMode);
        }
        setMeasuredDimension(widthSizeAndState, heightSizeAndState);
    }

    @Override
    public int getLargestMainSize() {
        int largestSize = Integer.MIN_VALUE;
        for (FlexLine flexLine : mFlexLines) {
            largestSize = Math.max(largestSize, flexLine.getMainSize());
        }
        return largestSize;
    }

    @Override
    public int getSumOfCrossSize() {
        int sum = 0;
        for (int i = 0, size = mFlexLines.size(); i < size; i++) {
            FlexLine flexLine = mFlexLines.get(i);

            // Judge if the beginning or middle dividers are required
            if (hasDividerBeforeFlexLine(i)) {
                if (isMainAxisDirectionHorizontal()) {
                    sum += mDividerHorizontalHeight;
                } else {
                    sum += mDividerVerticalWidth;
                }
            }

            // Judge if the end divider is required
            if (hasEndDividerAfterFlexLine(i)) {
                if (isMainAxisDirectionHorizontal()) {
                    sum += mDividerHorizontalHeight;
                } else {
                    sum += mDividerVerticalWidth;
                }
            }
            sum += flexLine.getCrossSize();
        }
        return sum;
    }

    @Override
    public boolean isMainAxisDirectionHorizontal() {
        return mFlexDirection == ROW || mFlexDirection == ROW_REVERSE;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        boolean isRtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
        flexContainer.layout(left, top, right, bottom, isRtl);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlexboxLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof FlexboxLayout.LayoutParams) {
            return new FlexboxLayout.LayoutParams((FlexboxLayout.LayoutParams) lp);
        } else if (lp instanceof MarginLayoutParams) {
            return new FlexboxLayout.LayoutParams((MarginLayoutParams) lp);
        }
        return new LayoutParams(lp);
    }

    @FlexDirection
    @Override
    public int getFlexDirection() {
        return mFlexDirection;
    }

    @Override
    public void setFlexDirection(@FlexDirection int flexDirection) {
        if (mFlexDirection != flexDirection) {
            mFlexDirection = flexDirection;
            requestLayout();
        }
    }

    @FlexWrap
    @Override
    public int getFlexWrap() {
        return mFlexWrap;
    }

    @Override
    public void setFlexWrap(@FlexWrap int flexWrap) {
        if (mFlexWrap != flexWrap) {
            mFlexWrap = flexWrap;
            requestLayout();
        }
    }

    @JustifyContent
    @Override
    public int getJustifyContent() {
        return mJustifyContent;
    }

    @Override
    public void setJustifyContent(@JustifyContent int justifyContent) {
        if (mJustifyContent != justifyContent) {
            mJustifyContent = justifyContent;
            requestLayout();
        }
    }

    @AlignItems
    @Override
    public int getAlignItems() {
        return mAlignItems;
    }

    @Override
    public void setAlignItems(@AlignItems int alignItems) {
        if (mAlignItems != alignItems) {
            mAlignItems = alignItems;
            requestLayout();
        }
    }

    @AlignContent
    @Override
    public int getAlignContent() {
        return mAlignContent;
    }

    @Override
    public void setAlignContent(@AlignContent int alignContent) {
        if (mAlignContent != alignContent) {
            mAlignContent = alignContent;
            requestLayout();
        }
    }

    @Override
    public int getMaxLine() {
        return mMaxLine;
    }

    @Override
    public void setMaxLine(int maxLine) {
        if (mMaxLine != maxLine) {
            mMaxLine = maxLine;
            requestLayout();
        }
    }

    /**
     * @return the flex lines composing this flex container. This method returns a copy of the
     * original list excluding a dummy flex line (flex line that doesn't have any flex items in it
     * but used for the alignment along the cross axis).
     * Thus any changes of the returned list are not reflected to the original list.
     */
    @Override
    public List<FlexLine> getFlexLines() {
        List<FlexLine> result = new ArrayList<>(mFlexLines.size());
        for (FlexLine flexLine : mFlexLines) {
            if (flexLine.getItemCount() == 0) {
                continue;
            }
            result.add(flexLine);
        }
        return result;
    }

    @Override
    public int getDecorationLengthMainAxis(View view, int index, int indexInFlexLine) {
        int decorationLength = 0;
        if (isMainAxisDirectionHorizontal()) {
            if (hasDividerBeforeChildAtAlongMainAxis(index, indexInFlexLine)) {
                decorationLength += mDividerVerticalWidth;
            }
            if ((mShowDividerVertical & SHOW_DIVIDER_END) > 0) {
                decorationLength += mDividerVerticalWidth;
            }
        } else {
            if (hasDividerBeforeChildAtAlongMainAxis(index, indexInFlexLine)) {
                decorationLength += mDividerHorizontalHeight;
            }
            if ((mShowDividerHorizontal & SHOW_DIVIDER_END) > 0) {
                decorationLength += mDividerHorizontalHeight;
            }
        }
        return decorationLength;
    }

    @Override
    public int getDecorationLengthCrossAxis(View view) {
        // Decoration along the cross axis for an individual view is not supported in the
        // FlexboxLayout.
        return 0;
    }

    @Override
    public void onNewFlexLineAdded(FlexLine flexLine) {
    }

    @Override
    public int getChildWidthMeasureSpec(int widthSpec, int padding, int childDimension) {
        return getChildMeasureSpec(widthSpec, padding, childDimension);
    }

    @Override
    public int getChildHeightMeasureSpec(int heightSpec, int padding, int childDimension) {
        return getChildMeasureSpec(heightSpec, padding, childDimension);
    }

    @Override
    public void onNewFlexItemAdded(View view, int index, int indexInFlexLine, FlexLine flexLine) {
    }

    @Override
    public void setFlexLines(List<FlexLine> flexLines) {
        mFlexLines = flexLines;
    }

    @Override
    public List<FlexLine> getFlexLinesInternal() {
        return mFlexLines;
    }

    @Override
    public void updateViewCache(int position, View view) {
        // No op
    }

    /**
     * @return the horizontal divider drawable that will divide each item.
     * @see #setDividerDrawable(Drawable)
     * @see #setDividerDrawableHorizontal(Drawable)
     */
    @Nullable
    @SuppressWarnings("UnusedDeclaration")
    public Drawable getDividerDrawableHorizontal() {
        return mDividerDrawableHorizontal;
    }

    /**
     * @return the vertical divider drawable that will divide each item.
     * @see #setDividerDrawable(Drawable)
     * @see #setDividerDrawableVertical(Drawable)
     */
    @Nullable
    @SuppressWarnings("UnusedDeclaration")
    public Drawable getDividerDrawableVertical() {
        return mDividerDrawableVertical;
    }

    /**
     * Set a drawable to be used as a divider between items. The drawable is used for both
     * horizontal and vertical dividers.
     *
     * @param divider Drawable that will divide each item for both horizontally and vertically.
     * @see #setShowDivider(int)
     */
    public void setDividerDrawable(Drawable divider) {
        setDividerDrawableHorizontal(divider);
        setDividerDrawableVertical(divider);
    }

    /**
     * Set a drawable to be used as a horizontal divider between items.
     *
     * @param divider Drawable that will divide each item.
     * @see #setDividerDrawable(Drawable)
     * @see #setShowDivider(int)
     * @see #setShowDividerHorizontal(int)
     */
    public void setDividerDrawableHorizontal(@Nullable Drawable divider) {
        if (divider == mDividerDrawableHorizontal) {
            return;
        }
        mDividerDrawableHorizontal = divider;
        if (divider != null) {
            mDividerHorizontalHeight = divider.getIntrinsicHeight();
        } else {
            mDividerHorizontalHeight = 0;
        }
        setWillNotDrawFlag();
        requestLayout();
    }

    /**
     * Set a drawable to be used as a vertical divider between items.
     *
     * @param divider Drawable that will divide each item.
     * @see #setDividerDrawable(Drawable)
     * @see #setShowDivider(int)
     * @see #setShowDividerVertical(int)
     */
    public void setDividerDrawableVertical(@Nullable Drawable divider) {
        if (divider == mDividerDrawableVertical) {
            return;
        }
        mDividerDrawableVertical = divider;
        if (divider != null) {
            mDividerVerticalWidth = divider.getIntrinsicWidth();
        } else {
            mDividerVerticalWidth = 0;
        }
        setWillNotDrawFlag();
        requestLayout();
    }

    @FlexboxLayout.DividerMode
    public int getShowDividerVertical() {
        return mShowDividerVertical;
    }

    @FlexboxLayout.DividerMode
    public int getShowDividerHorizontal() {
        return mShowDividerHorizontal;
    }

    /**
     * Set how dividers should be shown between items in this layout. This method sets the
     * divider mode for both horizontally and vertically.
     *
     * @param dividerMode One or more of {@link #SHOW_DIVIDER_BEGINNING},
     *                    {@link #SHOW_DIVIDER_MIDDLE}, or {@link #SHOW_DIVIDER_END},
     *                    or {@link #SHOW_DIVIDER_NONE} to show no dividers.
     * @see #setShowDividerVertical(int)
     * @see #setShowDividerHorizontal(int)
     */
    public void setShowDivider(@DividerMode int dividerMode) {
        setShowDividerVertical(dividerMode);
        setShowDividerHorizontal(dividerMode);
    }

    /**
     * Set how vertical dividers should be shown between items in this layout
     *
     * @param dividerMode One or more of {@link #SHOW_DIVIDER_BEGINNING},
     *                    {@link #SHOW_DIVIDER_MIDDLE}, or {@link #SHOW_DIVIDER_END},
     *                    or {@link #SHOW_DIVIDER_NONE} to show no dividers.
     * @see #setShowDivider(int)
     */
    public void setShowDividerVertical(@DividerMode int dividerMode) {
        if (dividerMode != mShowDividerVertical) {
            mShowDividerVertical = dividerMode;
            requestLayout();
        }
    }

    /**
     * Set how horizontal dividers should be shown between items in this layout.
     *
     * @param dividerMode One or more of {@link #SHOW_DIVIDER_BEGINNING},
     *                    {@link #SHOW_DIVIDER_MIDDLE}, or {@link #SHOW_DIVIDER_END},
     *                    or {@link #SHOW_DIVIDER_NONE} to show no dividers.
     * @see #setShowDivider(int)
     */
    public void setShowDividerHorizontal(@DividerMode int dividerMode) {
        if (dividerMode != mShowDividerHorizontal) {
            mShowDividerHorizontal = dividerMode;
            requestLayout();
        }
    }

    private void setWillNotDrawFlag() {
        if (mDividerDrawableHorizontal == null && mDividerDrawableVertical == null) {
            setWillNotDraw(true);
        } else {
            setWillNotDraw(false);
        }
    }

    /**
     * Check if a divider is needed before the view whose indices are passed as arguments.
     *
     * @param index           the absolute index of the view to be judged
     * @param indexInFlexLine the relative index in the flex line where the view
     *                        belongs
     * @return {@code true} if a divider is needed, {@code false} otherwise
     */
    private boolean hasDividerBeforeChildAtAlongMainAxis(int index, int indexInFlexLine) {
        if (allViewsAreGoneBefore(index, indexInFlexLine)) {
            if (isMainAxisDirectionHorizontal()) {
                return (mShowDividerVertical & SHOW_DIVIDER_BEGINNING) != 0;
            } else {
                return (mShowDividerHorizontal & SHOW_DIVIDER_BEGINNING) != 0;
            }
        } else {
            if (isMainAxisDirectionHorizontal()) {
                return (mShowDividerVertical & SHOW_DIVIDER_MIDDLE) != 0;
            } else {
                return (mShowDividerHorizontal & SHOW_DIVIDER_MIDDLE) != 0;
            }
        }
    }

    private boolean allViewsAreGoneBefore(int index, int indexInFlexLine) {
        for (int i = 1; i <= indexInFlexLine; i++) {
            View view = getReorderedChildAt(index - i);
            if (view != null && view.getVisibility() != View.GONE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a divider is needed before the flex line whose index is passed as an argument.
     *
     * @param flexLineIndex the index of the flex line to be checked
     * @return {@code true} if a divider is needed, {@code false} otherwise
     */
    private boolean hasDividerBeforeFlexLine(int flexLineIndex) {
        if (flexLineIndex < 0 || flexLineIndex >= mFlexLines.size()) {
            return false;
        }
        if (allFlexLinesAreDummyBefore(flexLineIndex)) {
            if (isMainAxisDirectionHorizontal()) {
                return (mShowDividerHorizontal & SHOW_DIVIDER_BEGINNING) != 0;
            } else {
                return (mShowDividerVertical & SHOW_DIVIDER_BEGINNING) != 0;
            }
        } else {
            if (isMainAxisDirectionHorizontal()) {
                return (mShowDividerHorizontal & SHOW_DIVIDER_MIDDLE) != 0;
            } else {
                return (mShowDividerVertical & SHOW_DIVIDER_MIDDLE) != 0;
            }
        }
    }

    private boolean allFlexLinesAreDummyBefore(int flexLineIndex) {
        for (int i = 0; i < flexLineIndex; i++) {
            if (mFlexLines.get(i).getItemCount() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a end divider is needed after the flex line whose index is passed as an argument.
     *
     * @param flexLineIndex the index of the flex line to be checked
     * @return {@code true} if a divider is needed, {@code false} otherwise
     */
    private boolean hasEndDividerAfterFlexLine(int flexLineIndex) {
        if (flexLineIndex < 0 || flexLineIndex >= mFlexLines.size()) {
            return false;
        }

        for (int i = flexLineIndex + 1; i < mFlexLines.size(); i++) {
            if (mFlexLines.get(i).getItemCount() > 0) {
                return false;
            }
        }
        if (isMainAxisDirectionHorizontal()) {
            return (mShowDividerHorizontal & SHOW_DIVIDER_END) != 0;
        } else {
            return (mShowDividerVertical & SHOW_DIVIDER_END) != 0;
        }

    }

    /**
     * Per child parameters for children views of the {@link FlexboxLayout}.
     *
     * Note that some parent fields (which are not primitive nor a class implements
     * {@link Parcelable}) are not included as the stored/restored fields after this class
     * is serialized/de-serialized as an {@link Parcelable}.
     */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams implements FlexItem {

        /**
         * @see FlexItem#getOrder()
         */
        private int mOrder = FlexItem.ORDER_DEFAULT;

        /**
         * @see FlexItem#getFlexGrow()
         */
        private float mFlexGrow = FlexItem.FLEX_GROW_DEFAULT;

        /**
         * @see FlexItem#getFlexShrink()
         */
        private float mFlexShrink = FlexItem.FLEX_SHRINK_DEFAULT;

        /**
         * @see FlexItem#getAlignSelf()
         */
        private int mAlignSelf = AlignSelf.AUTO;

        /**
         * @see FlexItem#getFlexBasisPercent()
         */
        private float mFlexBasisPercent = FlexItem.FLEX_BASIS_PERCENT_DEFAULT;

        /**
         * @see FlexItem#getMinWidth()
         */
        private int mMinWidth = NOT_SET;

        /**
         * @see FlexItem#getMinHeight()
         */
        private int mMinHeight = NOT_SET;

        /**
         * @see FlexItem#getMaxWidth()
         */
        private int mMaxWidth = MAX_SIZE;

        /**
         * @see FlexItem#getMaxHeight()
         */
        private int mMaxHeight = MAX_SIZE;

        /**
         * @see FlexItem#isWrapBefore()
         */
        private boolean mWrapBefore;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);

            TypedArray a = context
                    .obtainStyledAttributes(attrs, R.styleable.FlexboxLayout_Layout);
            mOrder = a.getInt(R.styleable.FlexboxLayout_Layout_layout_order, ORDER_DEFAULT);
            mFlexGrow = a
                    .getFloat(R.styleable.FlexboxLayout_Layout_layout_flexGrow, FLEX_GROW_DEFAULT);
            mFlexShrink = a.getFloat(R.styleable.FlexboxLayout_Layout_layout_flexShrink,
                    FLEX_SHRINK_DEFAULT);
            mAlignSelf = a
                    .getInt(R.styleable.FlexboxLayout_Layout_layout_alignSelf, AlignSelf.AUTO);
            mFlexBasisPercent = a
                    .getFraction(R.styleable.FlexboxLayout_Layout_layout_flexBasisPercent, 1, 1,
                            FLEX_BASIS_PERCENT_DEFAULT);
            mMinWidth = a
                    .getDimensionPixelSize(R.styleable.FlexboxLayout_Layout_layout_minWidth, NOT_SET);
            mMinHeight = a
                    .getDimensionPixelSize(R.styleable.FlexboxLayout_Layout_layout_minHeight, NOT_SET);
            mMaxWidth = a.getDimensionPixelSize(R.styleable.FlexboxLayout_Layout_layout_maxWidth,
                    MAX_SIZE);
            mMaxHeight = a.getDimensionPixelSize(R.styleable.FlexboxLayout_Layout_layout_maxHeight,
                    MAX_SIZE);
            mWrapBefore = a.getBoolean(R.styleable.FlexboxLayout_Layout_layout_wrapBefore, false);
            a.recycle();
        }

        public LayoutParams(LayoutParams source) {
            super(source);

            mOrder = source.mOrder;
            mFlexGrow = source.mFlexGrow;
            mFlexShrink = source.mFlexShrink;
            mAlignSelf = source.mAlignSelf;
            mFlexBasisPercent = source.mFlexBasisPercent;
            mMinWidth = source.mMinWidth;
            mMinHeight = source.mMinHeight;
            mMaxWidth = source.mMaxWidth;
            mMaxHeight = source.mMaxHeight;
            mWrapBefore = source.mWrapBefore;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(int width, int height) {
            super(new ViewGroup.LayoutParams(width, height));
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public void setWidth(int width) {
            this.width = width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public int getOrder() {
            return mOrder;
        }

        @Override
        public void setOrder(int order) {
            mOrder = order;
        }

        @Override
        public float getFlexGrow() {
            return mFlexGrow;
        }

        @Override
        public void setFlexGrow(float flexGrow) {
            this.mFlexGrow = flexGrow;
        }

        @Override
        public float getFlexShrink() {
            return mFlexShrink;
        }

        @Override
        public void setFlexShrink(float flexShrink) {
            this.mFlexShrink = flexShrink;
        }

        @AlignSelf
        @Override
        public int getAlignSelf() {
            return mAlignSelf;
        }

        @Override
        public void setAlignSelf(@AlignSelf int alignSelf) {
            this.mAlignSelf = alignSelf;
        }

        @Override
        public int getMinWidth() {
            return mMinWidth;
        }

        @Override
        public void setMinWidth(int minWidth) {
            this.mMinWidth = minWidth;
        }

        @Override
        public int getMinHeight() {
            return mMinHeight;
        }

        @Override
        public void setMinHeight(int minHeight) {
            this.mMinHeight = minHeight;
        }

        @Override
        public int getMaxWidth() {
            return mMaxWidth;
        }

        @Override
        public void setMaxWidth(int maxWidth) {
            this.mMaxWidth = maxWidth;
        }

        @Override
        public int getMaxHeight() {
            return mMaxHeight;
        }

        @Override
        public void setMaxHeight(int maxHeight) {
            this.mMaxHeight = maxHeight;
        }

        @Override
        public boolean isWrapBefore() {
            return mWrapBefore;
        }

        @Override
        public void setWrapBefore(boolean wrapBefore) {
            this.mWrapBefore = wrapBefore;
        }

        @Override
        public float getFlexBasisPercent() {
            return mFlexBasisPercent;
        }

        @Override
        public void setFlexBasisPercent(float flexBasisPercent) {
            this.mFlexBasisPercent = flexBasisPercent;
        }

        @Override
        public int getMarginLeft() {
            return leftMargin;
        }

        @Override
        public int getMarginTop() {
            return topMargin;
        }

        @Override
        public int getMarginRight() {
            return rightMargin;
        }

        @Override
        public int getMarginBottom() {
            return bottomMargin;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mOrder);
            dest.writeFloat(this.mFlexGrow);
            dest.writeFloat(this.mFlexShrink);
            dest.writeInt(this.mAlignSelf);
            dest.writeFloat(this.mFlexBasisPercent);
            dest.writeInt(this.mMinWidth);
            dest.writeInt(this.mMinHeight);
            dest.writeInt(this.mMaxWidth);
            dest.writeInt(this.mMaxHeight);
            dest.writeByte(this.mWrapBefore ? (byte) 1 : (byte) 0);
            dest.writeInt(this.bottomMargin);
            dest.writeInt(this.leftMargin);
            dest.writeInt(this.rightMargin);
            dest.writeInt(this.topMargin);
            dest.writeInt(this.height);
            dest.writeInt(this.width);
        }

        protected LayoutParams(Parcel in) {
            // Passing a resolved value to resolve a lint warning
            // height and width are set in this method anyway.
            super(0, 0);
            this.mOrder = in.readInt();
            this.mFlexGrow = in.readFloat();
            this.mFlexShrink = in.readFloat();
            this.mAlignSelf = in.readInt();
            this.mFlexBasisPercent = in.readFloat();
            this.mMinWidth = in.readInt();
            this.mMinHeight = in.readInt();
            this.mMaxWidth = in.readInt();
            this.mMaxHeight = in.readInt();
            this.mWrapBefore = in.readByte() != 0;
            this.bottomMargin = in.readInt();
            this.leftMargin = in.readInt();
            this.rightMargin = in.readInt();
            this.topMargin = in.readInt();
            this.height = in.readInt();
            this.width = in.readInt();
        }

        public static final Parcelable.Creator<LayoutParams> CREATOR
                = new Parcelable.Creator<LayoutParams>() {
            @Override
            public LayoutParams createFromParcel(Parcel source) {
                return new LayoutParams(source);
            }

            @Override
            public LayoutParams[] newArray(int size) {
                return new LayoutParams[size];
            }
        };
    }
}
