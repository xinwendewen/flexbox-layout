package com.xinwendewen.flexbox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlexLine {
    public FlexLine() {
    }

    public static FlexLine createDummyWithCrossSize(int crossSize) {
        FlexLine flexLine = new FlexLine();
        flexLine.mCrossSize = crossSize;
        return flexLine;
    }

    public FlexItem getItemAt(int index) {
       return items.get(index);
    }

    public List<FlexItem> items = new ArrayList<>();

    public void addItem(FlexItem item, boolean isMainAxisHorizontal) {
        items.add(item);
        mItemCount++;
        mAnyItemsHaveFlexGrow |= item.getFlexGrow() > 0;
        mAnyItemsHaveFlexShrink |= item.getFlexShrink() > 0;
        mMainSize += item.getOuterMainSize(isMainAxisHorizontal);
        mTotalFlexGrow += item.getFlexGrow();
        mTotalFlexShrink += item.getFlexShrink();
        mCrossSize = Math.max(mCrossSize, item.getOuterCrossSize(isMainAxisHorizontal));
    }

    public int mMainSize;

    public int mCrossSize;

    public int mItemCount;

    int mGoneItemCount;

    public float mTotalFlexGrow;

    public float mTotalFlexShrink;

    public int mSumCrossSizeBefore;

    public boolean mAnyItemsHaveFlexGrow;

    public boolean mAnyItemsHaveFlexShrink;

    public int getMainSize() {
        return mMainSize;
    }

    public int getCrossSize() {
        return mCrossSize;
    }

    public int getItemCount() {
        return items.size();
    }

    public int getItemCountNotGone() {
        return mItemCount - mGoneItemCount;
    }

    public boolean isFrozen() {
        for (int i = 0; i < mItemCount; i++) {
            FlexItem item = items.get(i);
            boolean isItemFrozen = !item.isFlexible() || violatedIndices.contains(i);
            if (!isItemFrozen) {
                return false;
            }
        }
        return true;
    }

    Set<Integer> violatedIndices = new HashSet<>();


    public boolean isItemShrinkFrozen(int index) {
        if (violatedIndices.contains(index)) {
            return true;
        }
        FlexItem item = items.get(index);
        float flexShrink = item.getFlexShrink();
        if (flexShrink <= 0) {
            return true;
        }
        return false;
    }

    public boolean isItemGrowFrozen(int index) {
        if (violatedIndices.contains(index)) {
            return true;
        }
        FlexItem item = items.get(index);
        float flexGrow = item.getFlexGrow();
        if (flexGrow <= 0) {
            return true;
        }
        return false;
    }

    public void freezeItemAt(int index) {
        violatedIndices.add(index);
        FlexItem item = items.get(index);
        float growFactor = item.getFlexGrow();
        if (growFactor > 0) {
            mTotalFlexGrow -= growFactor;
        }
        float shrinkFactor = item.getFlexShrink();
        if (shrinkFactor > 0) {
            mTotalFlexShrink -= shrinkFactor;
        }
    }

    public void refreshCrossSize(boolean isMainAxisHorizontal) {
        int crossSize = 0;
        for (FlexItem item : items) {
            crossSize = Math.max(crossSize, item.getOuterCrossSize(isMainAxisHorizontal));
        }
    }
}
