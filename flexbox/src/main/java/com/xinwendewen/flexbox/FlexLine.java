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

package com.xinwendewen.flexbox;

import static com.google.android.flexbox.FlexContainer.NOT_SET;
import static com.google.android.flexbox.FlexItem.FLEX_GROW_DEFAULT;
import static com.google.android.flexbox.FlexItem.FLEX_SHRINK_NOT_SET;

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
        mAnyItemsHaveFlexGrow |= item.getFlexGrow() != FLEX_GROW_DEFAULT;
        mAnyItemsHaveFlexShrink |= item.getFlexShrink() != FLEX_SHRINK_NOT_SET;
        mMainSize += item.getOuterMainSize(isMainAxisHorizontal);
        mTotalFlexGrow += item.getFlexGrow();
        mTotalFlexShrink += item.getFlexShrink();
        mCrossSize = Math.max(mCrossSize, item.getOuterCrossSize(isMainAxisHorizontal));
    }

    public int mMainSize;

    public int mCrossSize;

    public int mItemCount;

    /** Holds the count of the views whose visibilities are gone */
    int mGoneItemCount;

    public float mTotalFlexGrow;

    public float mTotalFlexShrink;

    /**
     * The sum of the cross size used before this flex line.
     */
    public int mSumCrossSizeBefore;

    /**
     * Set to true if any {@link com.google.android.flexbox.FlexItem}s in this line have {@link com.google.android.flexbox.FlexItem#getFlexGrow()}
     * attributes set (have the value other than {@link com.google.android.flexbox.FlexItem#FLEX_GROW_DEFAULT})
     */
    public boolean mAnyItemsHaveFlexGrow;

    /**
     * Set to true if any {@link com.google.android.flexbox.FlexItem}s in this line have {@link com.google.android.flexbox.FlexItem#getFlexShrink()}
     * attributes set (have the value other than {@link com.google.android.flexbox.FlexItem#FLEX_SHRINK_NOT_SET})
     */
    public boolean mAnyItemsHaveFlexShrink;

    /**
     * @return the size of the flex line in pixels along the main axis of the flex container.
     */
    public int getMainSize() {
        return mMainSize;
    }

    /**
     * @return the size of the flex line in pixels along the cross axis of the flex container.
     */
    @SuppressWarnings("WeakerAccess")
    public int getCrossSize() {
        return mCrossSize;
    }

    /**
     * @return the count of the views contained in this flex line.
     */
    @SuppressWarnings("WeakerAccess")
    public int getItemCount() {
        return items.size();
    }

    /**
     * @return the count of the views whose visibilities are not gone in this flex line.
     */
    @SuppressWarnings("WeakerAccess")
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
        if (growFactor != NOT_SET) {
            mTotalFlexGrow -= growFactor;
        }
        float shrinkFactor = item.getFlexShrink();
        if (shrinkFactor != NOT_SET) {
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
