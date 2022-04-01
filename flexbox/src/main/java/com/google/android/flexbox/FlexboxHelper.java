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

import static com.google.android.flexbox.AlignItems.CENTER;
import static com.google.android.flexbox.AlignItems.FLEX_END;
import static com.google.android.flexbox.AlignItems.FLEX_START;
import static com.google.android.flexbox.AlignItems.STRETCH;
import static com.google.android.flexbox.FlexDirection.COLUMN;
import static com.google.android.flexbox.FlexDirection.COLUMN_REVERSE;
import static com.google.android.flexbox.FlexDirection.ROW;
import static com.google.android.flexbox.FlexDirection.ROW_REVERSE;
import static com.google.android.flexbox.FlexWrap.NOWRAP;
import static com.xinwendewen.flexbox.MeasureRequestUtils.isUnspecifiedMode;

import com.xinwendewen.flexbox.ContainerProperties;
import com.xinwendewen.flexbox.FlexLine;
import com.xinwendewen.flexbox.FlexLines;
import com.xinwendewen.flexbox.MeasureRequestUtils;
import com.xinwendewen.flexbox.NewFlexItem;
import com.xinwendewen.flexbox.RoundingErrorAccumulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Offers various calculations for Flexbox to use the common logic between the classes such as
 * {@link FlexboxLayout}.
 */
class FlexboxHelper {
    private final FlexContainer mFlexContainer;

    FlexboxHelper(FlexContainer flexContainer) {
        mFlexContainer = flexContainer;
    }

    void fillFlexLines(FlexLines result, int widthMeasureSpec,
                       int heightMeasureSpec) {
        ContainerProperties containerProperties = new ContainerProperties(mFlexContainer,
                widthMeasureSpec, heightMeasureSpec);
        boolean isMainAxisHorizontal = containerProperties.isMainAxisHorizontal();
        int mainMeasureSpec;
        int crossMeasureSpec;
        if (isMainAxisHorizontal) {
            mainMeasureSpec = widthMeasureSpec;
            crossMeasureSpec = heightMeasureSpec;
        } else {
            mainMeasureSpec = heightMeasureSpec;
            crossMeasureSpec = widthMeasureSpec;
        }
        List<FlexLine> flexLines = fillFlexLines(mainMeasureSpec, crossMeasureSpec,
                containerProperties);
        result.mFlexLines = flexLines;
    }

    List<FlexLine> fillFlexLines(int containerMainMeasureSpec, int containerCrossMeasureSpec,
                                 ContainerProperties containerProps) {
        // prepare flex lines
        List<FlexLine> flexLines = new ArrayList<>();
        // prepare current flex line
        FlexLine currentFlexLine = new FlexLine(containerProps.getMainPaddings());

        boolean isMainAxisHorizontal = containerProps.isMainAxisHorizontal();
        int occupiedContainerCrossSize = containerProps.getCrossPaddings();
        for (int i = 0; i < mFlexContainer.getFlexItemCount(); i++) {
            NewFlexItem item = mFlexContainer.getReorderedNewFlexItemAt(i);
            // measure flex item
            int occupiedMainSize = containerProps.getMainPaddings();
            item.measure(containerMainMeasureSpec, occupiedMainSize, containerCrossMeasureSpec,
                    occupiedContainerCrossSize, isMainAxisHorizontal);
            // clamp by min/max constraints and remeasure if needed
            item.clampByMinMaxCrossSize();
            if (isWrapNeeded(containerMainMeasureSpec, containerProps, currentFlexLine, item,
                    isMainAxisHorizontal)) {
                // finish current flex line
                currentFlexLine.mSumCrossSizeBefore = occupiedContainerCrossSize;
                flexLines.add(currentFlexLine);
                occupiedContainerCrossSize += currentFlexLine.mCrossSize;
                // remeasure if cross size MATCH_PARENT
                if (item.requireCrossSizeMatchParent(isMainAxisHorizontal)) {
                    item.measure(containerMainMeasureSpec, occupiedMainSize,
                            containerCrossMeasureSpec, occupiedContainerCrossSize, isMainAxisHorizontal);
                }
                // prepare new flex line
                currentFlexLine = new FlexLine(containerProps.getMainPaddings());
            }
            // add current item
            currentFlexLine.addItem(item, isMainAxisHorizontal);
        }
        flexLines.add(currentFlexLine);
        return flexLines;
    }

    private boolean isWrapNeeded(int containerMainMeasureSpec, ContainerProperties containerProps,
                                 FlexLine currentFlexLine, NewFlexItem item,
                                 boolean isMainAxisHorizontal) {
        if (containerProps.getFlexWrap() == NOWRAP) {
            return false;
        }
        if (isUnspecifiedMode(containerMainMeasureSpec)) {
            return false;
        }
        return MeasureRequestUtils.getMeasureSpecSize(containerMainMeasureSpec)
                < currentFlexLine.mMainSize + item.getOuterMainSize(isMainAxisHorizontal);
    }

    int determineMainSize(ContainerProperties properties, FlexLines flexLines) {
        int largestFlexLineMainSize = flexLines.getLargestMainSize();
        int expectedMainSize = properties.getExpectedMainSize();
        if (properties.requireFixedMainSize()) {
            return expectedMainSize;
        } else {
            return Math.min(largestFlexLineMainSize, expectedMainSize);
        }
    }

    /**
     * Determine the main size by expanding (shrinking if negative remaining free space is given)
     * an individual child in each flex line if any children's mFlexGrow (or mFlexShrink if
     * remaining
     * space is negative) properties are set to non-zero.
     *
     * @param widthMeasureSpec  horizontal space requirements as imposed by the parent
     * @param heightMeasureSpec vertical space requirements as imposed by the parent
     * @see FlexContainer#setFlexDirection(int)
     * @see FlexContainer#getFlexDirection()
     */
    int determineMainSize(int widthMeasureSpec, int heightMeasureSpec, FlexLines flexLinesResult) {
        ContainerProperties containerProps = new ContainerProperties(mFlexContainer,
                widthMeasureSpec, heightMeasureSpec);
        return determineMainSize(containerProps, flexLinesResult);
    }

    void calculateFlexibleLength(int mainSize, int widthMeasureSpec, int heightMeasureSpec) {
        ContainerProperties containerProps = new ContainerProperties(mFlexContainer,
                widthMeasureSpec, heightMeasureSpec);
        List<FlexLine> flexLines = mFlexContainer.getFlexLinesInternal();
        for (int i = 0, size = flexLines.size(); i < size; i++) {
            FlexLine flexLine = flexLines.get(i);
            if (flexLine.mMainSize < mainSize && flexLine.mAnyItemsHaveFlexGrow) {
                calculateFlexibleLength(flexLine, mainSize, containerProps);
            } else if (flexLine.mMainSize > mainSize && flexLine.mAnyItemsHaveFlexShrink) {
                calculateFlexibleLength(flexLine, mainSize, containerProps);
            }
        }
    }

    private void calculateFlexibleLength(FlexLine flexLine, int containerMainSize,
                                         ContainerProperties containerProps) {
        RoundingErrorAccumulator errorAccumulator = new RoundingErrorAccumulator();
        boolean isMainAxisHorizontal = containerProps.isMainAxisHorizontal();
        while (!flexLine.isFrozen() && flexLine.mMainSize != containerMainSize) {
            int available = containerMainSize - flexLine.mMainSize;
            float spaceUnit = available / (available > 0 ? flexLine.mTotalFlexGrow : flexLine.mTotalFlexShrink);
            boolean hasViolation = false;
            for (int i = 0; i < flexLine.mItemCount; i++) {
                NewFlexItem item = flexLine.getItemAt(i);
                if (available < 0 && flexLine.isItemShrinkFrozen(i)) {
                    continue;
                }
                if (available > 0 && flexLine.isItemGrowFrozen(i)) {
                    continue;
                }
                flexLine.mMainSize -= item.getOuterMainSize(isMainAxisHorizontal);
                int measuredMainSize =
                        item.getMainSize(isMainAxisHorizontal);
                float newMainSize = measuredMainSize + spaceUnit * (available > 0 ?
                        item.getFlexGrow() : item.getFlexShrink());
                if (newMainSize < item.minMainSize(isMainAxisHorizontal)) {
                    hasViolation = true;
                    newMainSize = item.minMainSize(isMainAxisHorizontal);
                    flexLine.freezeItemAt(i);
                } else if (newMainSize > item.maxMainSize(isMainAxisHorizontal)) {
                    hasViolation = true;
                    newMainSize = item.maxMainSize(isMainAxisHorizontal);
                    flexLine.freezeItemAt(i);
                }
                int roundedNewMainSize = errorAccumulator.round(newMainSize);
                roundedNewMainSize += errorAccumulator.compensate();
                item.fixedMainSizeMeasure(containerProps, roundedNewMainSize,
                        flexLine.mSumCrossSizeBefore);
                item.clampByMinMaxCrossSize();
                flexLine.mMainSize += item.getOuterMainSize(isMainAxisHorizontal);
            }
            if (!hasViolation) {
                break;
            }
        }
        flexLine.refreshCrossSize(isMainAxisHorizontal);
    }

    void stretchItems() {
        List<FlexLine> flexLines = mFlexContainer.getFlexLinesInternal();
        for (FlexLine flexLine : flexLines) {
            for (NewFlexItem item : flexLine.items) {
                if (needStretch(item, mFlexContainer.getAlignItems(), flexLine.mCrossSize,
                        mFlexContainer.isMainAxisDirectionHorizontal())) {
                   stretchItem(item, flexLine, mFlexContainer.isMainAxisDirectionHorizontal());
                }
            }
        }
    }

    private void stretchItem(NewFlexItem item, FlexLine flexLine, boolean isMainAxisHorizontal) {
        int newCrossSize = flexLine.mCrossSize - item.crossAxisMargin(isMainAxisHorizontal);
        newCrossSize = item.clampByMinMaxCrossSize(newCrossSize, isMainAxisHorizontal);
        item.fixedSizeMeasure(item.getMainSize(isMainAxisHorizontal), newCrossSize, isMainAxisHorizontal);
    }

    private boolean needStretch(NewFlexItem item, int containerAlignItems, int flexLineCrossSize,
                                boolean isMainAxisHorizontal) {
        if (item.getOuterCrossSize(isMainAxisHorizontal) >= flexLineCrossSize) {
            return false;
        }
        if (item.getAlignSelf() == AlignSelf.STRETCH) {
            return true;
        }
        if (item.getAlignSelf() == AlignSelf.AUTO && containerAlignItems == STRETCH) {
            return true;
        }
        return false;
    }

    public void crossAlignment(int containerInnerCrossSize, FlexLines mFlexLinesResult) {
        int flexLinesCrossSize = mFlexLinesResult.getCrossSize();
        int freeSpace = containerInnerCrossSize - flexLinesCrossSize;
        switch (mFlexContainer.getAlignContent()) {
            case AlignContent.FLEX_START:
                // do nothing
                break;
            case AlignContent.FLEX_END:
                alignContentFlexEnd(mFlexLinesResult, freeSpace);
                break;
            case AlignContent.STRETCH:
                alignContentStretch(mFlexLinesResult, freeSpace);
                break;
            case AlignContent.CENTER:
                alignContentCenter(mFlexLinesResult, freeSpace);
                break;
            case AlignContent.SPACE_AROUND:
                alignContentSpaceAround(mFlexLinesResult, freeSpace);
                break;
            case AlignContent.SPACE_BETWEEN:
                alignContentSpaceBetween(mFlexLinesResult, freeSpace);
                break;
        }
    }

    private void alignContentSpaceAround(FlexLines mFlexLinesResult, int freeSpace) {
        if (freeSpace > 0) {
            float unitSpace = (float) freeSpace / (mFlexLinesResult.size() * 2);
            mFlexLinesResult.insertAround(unitSpace);
        } else {
            alignContentCenter(mFlexLinesResult, freeSpace);
        }
    }

    private void alignContentCenter(FlexLines mFlexLinesResult, int freeSpace) {
        int unitSpace = freeSpace / 2;
        mFlexLinesResult.addTop(FlexLine.createDummyWithCrossSize(unitSpace));
        mFlexLinesResult.addBottom(FlexLine.createDummyWithCrossSize(unitSpace));
    }

    private void alignContentStretch(FlexLines mFlexLinesResult, int freeSpace) {
        if (freeSpace > 0) {
            int unitSpace = freeSpace / mFlexLinesResult.size();
            for (FlexLine flexLine : mFlexLinesResult.mFlexLines) {
               flexLine.mCrossSize += unitSpace;
            }
        }
    }

    private void alignContentFlexEnd(FlexLines mFlexLinesResult, int freeSpace) {
        mFlexLinesResult.addTop(FlexLine.createDummyWithCrossSize(freeSpace));
    }

    private void alignContentSpaceBetween(FlexLines mFlexLinesResult, int freeSpace) {
        if (freeSpace > 0) {
            float unitSpace = (float) freeSpace / (mFlexLinesResult.size() - 1);
            mFlexLinesResult.insertBetweenFlexLines(unitSpace);
        }
    }

    public int getExpectedContainerCrossSize(ContainerProperties containerProps) {
        return containerProps.getExpectedCrossSize();
    }

    void layout(int left, int top, int right, int bottom, boolean isRtl, int paddingLeft,
                int paddingTop, int paddingRight, int paddingBottom) {
        int width = right - left;
        int innerWidth = width - paddingLeft - paddingRight;
        int height = bottom - top;
        int innerHeight = height - paddingTop - paddingBottom;
        int containerInnerMainSize;
        int containerInnerCrossSize;
        boolean isMainAxisHorizontal = mFlexContainer.isMainAxisDirectionHorizontal();
        if (isMainAxisHorizontal) {
            containerInnerMainSize = innerWidth;
            containerInnerCrossSize = innerHeight;
        } else {
            containerInnerMainSize = innerHeight;
            containerInnerCrossSize = innerWidth;
        }
        boolean isMainAxisReversed = false;
        boolean isCrossAxisReversed = false;
        int flexDirection = mFlexContainer.getFlexDirection();
        int flexWrap = mFlexContainer.getFlexWrap();
        switch (flexDirection) {
            case ROW:
                if (isRtl) {
                    isMainAxisReversed = true;
                }
                if (flexWrap == FlexWrap.WRAP_REVERSE) {
                    isCrossAxisReversed = true;
                }
                break;
            case ROW_REVERSE:
                if (!isRtl) {
                    isMainAxisReversed = true;
                }
                if (flexWrap == FlexWrap.WRAP_REVERSE) {
                    isCrossAxisReversed = true;
                }
                break;
            case COLUMN:
                if (!isRtl && flexWrap == FlexWrap.WRAP_REVERSE) {
                    isCrossAxisReversed = true;
                }
                if (isRtl && flexWrap == FlexWrap.WRAP) {
                    isCrossAxisReversed = true;
                }
                break;
            case COLUMN_REVERSE:
                if (!isRtl && flexWrap == FlexWrap.WRAP_REVERSE) {
                    isCrossAxisReversed = true;
                }
                if (isRtl && flexWrap == FlexWrap.WRAP) {
                    isCrossAxisReversed = true;
                }
                isMainAxisReversed = true;
        }
        int crossAxisAnchor = isCrossAxisReversed ? containerInnerCrossSize : 0;
        List<FlexLine> flexLines = mFlexContainer.getFlexLinesInternal();
        for (FlexLine flexLine : flexLines) {
            int justifyContent = mFlexContainer.getJustifyContent();
            int flexLineMainSize = flexLine.mMainSize - paddingRight - paddingRight; // TODO: 2022/3/23 remove padding
            int mainAxisAnchor = 0;
            float spaceBetweenItems = 0;
            switch (justifyContent) {
                case JustifyContent.FLEX_START:
                    mainAxisAnchor = isMainAxisReversed ? containerInnerMainSize : 0;
                    break;
                case JustifyContent.FLEX_END:
                    mainAxisAnchor = isMainAxisReversed ? flexLineMainSize :
                            containerInnerMainSize - flexLineMainSize;
                    break;
                case JustifyContent.CENTER:
                    mainAxisAnchor = isMainAxisReversed ?
                            (containerInnerMainSize + flexLineMainSize) / 2 :
                            (containerInnerMainSize - flexLineMainSize) / 2;
                    break;
                case JustifyContent.SPACE_AROUND:
                    spaceBetweenItems =
                            (float) (containerInnerMainSize - flexLineMainSize) / flexLine.getItemCount();
                    mainAxisAnchor = isMainAxisReversed ?
                            containerInnerMainSize - Math.round(spaceBetweenItems) / 2 :
                            Math.round(spaceBetweenItems) / 2;
                    break;
                case JustifyContent.SPACE_BETWEEN:
                    spaceBetweenItems = flexLine.getItemCount() > 1 ?
                            (float) (containerInnerMainSize - flexLineMainSize) / (flexLine.getItemCount() - 1) : 0;
                    mainAxisAnchor = isMainAxisReversed ? containerInnerMainSize : 0;
                    break;
                case JustifyContent.SPACE_EVENLY:
                    spaceBetweenItems =
                            (float) (containerInnerMainSize - flexLineMainSize) / (flexLine.getItemCount() + 1);
                    mainAxisAnchor = isMainAxisReversed ?
                            Math.round(containerInnerMainSize - spaceBetweenItems) :
                            Math.round(spaceBetweenItems);
                    break;
            }
            RoundingErrorAccumulator errorAccumulator = new RoundingErrorAccumulator();
            for (int i = 0; i < flexLine.getItemCount(); i++) {
                NewFlexItem item = flexLine.getItemAt(i);
                layoutItem(item, isMainAxisReversed, mainAxisAnchor, isCrossAxisReversed,
                        crossAxisAnchor, isMainAxisHorizontal, mFlexContainer.getAlignItems(),
                        flexLine, paddingLeft, paddingTop);
                mainAxisAnchor = forwardMainAxisAnchor(mainAxisAnchor, isMainAxisReversed, item,
                        errorAccumulator.round(spaceBetweenItems) + errorAccumulator.compensate(),
                        isMainAxisHorizontal);
            }
            crossAxisAnchor = forwardCrossAxisAnchor(crossAxisAnchor, isCrossAxisReversed, flexLine);
        }
    }

    private int forwardCrossAxisAnchor(int crossAxisAnchor, boolean isCrossAxisReversed, FlexLine flexLine) {
        return isCrossAxisReversed ? crossAxisAnchor - flexLine.getCrossSize() : crossAxisAnchor + flexLine.getCrossSize();
    }

    private int forwardMainAxisAnchor(int mainAxisAnchor, boolean isMainAxisReversed,
                                      NewFlexItem item, int spaceBetweenItems,
                                      boolean isMainAxisHorizontal) {
        return isMainAxisReversed ?
                mainAxisAnchor - (item.mainAxisMarginStart(isMainAxisHorizontal) + item.getMainSize(isMainAxisHorizontal) + spaceBetweenItems) :
                mainAxisAnchor + (item.mainAxisMarginEnd(isMainAxisHorizontal) + item.getMainSize(isMainAxisHorizontal) + spaceBetweenItems);
    }

    void layoutItem(NewFlexItem item, boolean isMainAxisReversed, int mainAxisAnchor,
                    boolean isCrossAxisReversed, int crossAxisAnchor,
                    boolean isMainAxisHorizontal, int alignItems, FlexLine flexLine,
                    int leftPadding, int topPadding) {
        int mainStart;
        int mainEnd;
        int crossStart;
        int crossEnd;
        if (isMainAxisReversed) {
            mainEnd = mainAxisAnchor - item.mainAxisMarginEnd(isMainAxisHorizontal);
            mainStart = mainEnd - item.getMainSize(isMainAxisHorizontal);
        } else {
            mainStart = mainAxisAnchor + item.mainAxisMarginStart(isMainAxisHorizontal);
            mainEnd = mainStart + item.getMainSize(isMainAxisHorizontal);
        }
        int crossAlignment = alignItems;
        int alignSelf = item.getAlignSelf();
        if (alignSelf != AlignSelf.AUTO) {
            crossAlignment = alignSelf;
        }
        switch (crossAlignment) {
            case STRETCH:
            case FLEX_START:
                if (isCrossAxisReversed) {
                    crossEnd = crossAxisAnchor - item.crossAxisMarginEnd(isMainAxisHorizontal);
                    crossStart = crossEnd - item.getCrossSize(isMainAxisHorizontal);
                } else {
                    crossStart = crossAxisAnchor + item.crossAxisMarginStart(isMainAxisHorizontal);
                    crossEnd = crossStart + item.getCrossSize(isMainAxisHorizontal);
                }
                break;
            case FLEX_END:
                if (isCrossAxisReversed) {
                    crossStart =
                            crossAxisAnchor - flexLine.mCrossSize + item.crossAxisMarginStart(isMainAxisHorizontal);
                    crossEnd = crossStart + item.getCrossSize(isMainAxisHorizontal);
                } else {
                    crossEnd =
                            crossAxisAnchor + flexLine.mCrossSize - item.crossAxisMarginEnd(isMainAxisHorizontal);
                    crossStart = crossEnd - item.getCrossSize(isMainAxisHorizontal);
                }
                break;
            case CENTER:
                if (isCrossAxisReversed) {
                    crossEnd =
                            crossAxisAnchor - (flexLine.mCrossSize / 2 -
                                    item.getOuterCrossSize(isMainAxisHorizontal) / 2) -
                                    item.crossAxisMarginEnd(isMainAxisHorizontal);
                    crossStart = crossEnd - item.getCrossSize(isMainAxisHorizontal);
                } else {
                    crossStart =
                            crossAxisAnchor + flexLine.mCrossSize / 2 -
                                    item.getOuterCrossSize(isMainAxisHorizontal) / 2 +
                                    item.crossAxisMarginStart(isMainAxisHorizontal);
                    crossEnd = crossStart + item.getCrossSize(isMainAxisHorizontal);
                }
                break;
            default:
                throw new IllegalStateException();
        }
        item.layout(mainStart, mainEnd, crossStart, crossEnd, isMainAxisHorizontal, leftPadding,
                topPadding);
    }
}
