package com.xinwendewen.flexbox;

import com.google.android.flexbox.AlignSelf;

import java.util.ArrayList;
import java.util.List;

public class FlexContainerImpl implements FlexContainer {
    private List<NewFlexItem> items = new ArrayList<>();
    private final FlexLines flexLines = new FlexLines();
    private final ContainerFlexProperties flexProperties = new ContainerFlexProperties();
    private final Paddings paddings = new Paddings();

    @Override
    public List<FlexLine> getFlexLines() {
        return flexLines.mFlexLines;
    }

    @Override
    public void setFlexDirection(FlexDirection flexDirection) {
        flexProperties.flexDirection = flexDirection;
    }

    @Override
    public void setFlexWrap(FlexWrap flexWrap) {
        flexProperties.flexWrap = flexWrap;
    }

    @Override
    public void setJustifyContent(JustifyContent justifyContent) {
        flexProperties.justifyContent = justifyContent;
    }

    @Override
    public void setAlignContent(AlignContent alignContent) {
        flexProperties.alignContent = alignContent;
    }

    @Override
    public void setAlignItems(AlignItems alignItems) {
        flexProperties.alignItems = alignItems;
    }

    @Override
    public void setPaddings(Paddings paddings) {
        this.paddings.update(paddings);
    }

    @Override
    public void setFlexItems(List<NewFlexItem> flexItems) {
        items = flexItems;
    }

    @Override
    public void measure(MeasureRequest mainAxisMeasureRequest, MeasureRequest crossAxisMeasureRequest) {
        flexLines.reset();
        flexLines.mFlexLines = fillFlexLines(mainAxisMeasureRequest, crossAxisMeasureRequest);
        int mainSize = determineMainSize(mainAxisMeasureRequest);
        calculateFlexibleLength(mainSize, crossAxisMeasureRequest);
        if (crossAxisMeasureRequest.isTight()) {
            if (flexLines.isSingleLine()) {
                flexLines.mFlexLines.get(0).mCrossSize =
                        crossAxisMeasureRequest.intentSize() - paddings.getCrossPaddings(isMainAxisHorizontal());
            } else {
                int determinedCrossSize = crossAxisMeasureRequest.intentSize();
                int containerCrossAxisPadding = paddings.getCrossPaddings(isMainAxisHorizontal());
                crossAlignment(determinedCrossSize - containerCrossAxisPadding, flexLines);
            }
        }
        stretchItems();
    }

    void stretchItems() {
        for (FlexLine flexLine : flexLines.mFlexLines) {
            for (NewFlexItem item : flexLine.items) {
                if (needStretch(item, flexProperties.alignItems, flexLine.mCrossSize,
                        isMainAxisHorizontal())) {
                    stretchItem(item, flexLine, isMainAxisHorizontal());
                }
            }
        }
    }

    private void stretchItem(NewFlexItem item, FlexLine flexLine, boolean isMainAxisHorizontal) {
        int newCrossSize = flexLine.mCrossSize - item.crossAxisMargin(isMainAxisHorizontal);
        newCrossSize = item.clampByMinMaxCrossSize(newCrossSize, isMainAxisHorizontal);
        item.fixedSizeMeasure(item.getMainSize(isMainAxisHorizontal), newCrossSize, isMainAxisHorizontal);
    }

    private boolean needStretch(NewFlexItem item, AlignItems alignItems, int flexLineCrossSize,
                                boolean isMainAxisHorizontal) {
        if (item.getOuterCrossSize(isMainAxisHorizontal) >= flexLineCrossSize) {
            return false;
        }
        if (item.getAlignSelf() == com.google.android.flexbox.AlignSelf.STRETCH) {
            return true;
        }
        if (item.getAlignSelf() == AlignSelf.AUTO && alignItems == AlignItems.STRETCH) {
            return true;
        }
        return false;
    }

    public void crossAlignment(int containerInnerCrossSize, FlexLines mFlexLinesResult) {
        int flexLinesCrossSize = mFlexLinesResult.getCrossSize();
        int freeSpace = containerInnerCrossSize - flexLinesCrossSize;
        switch (flexProperties.alignContent) {
            case FLEX_START:
                // do nothing
                break;
            case FLEX_END:
                alignContentFlexEnd(mFlexLinesResult, freeSpace);
                break;
            case STRETCH:
                alignContentStretch(mFlexLinesResult, freeSpace);
                break;
            case CENTER:
                alignContentCenter(mFlexLinesResult, freeSpace);
                break;
            case SPACE_AROUND:
                alignContentSpaceAround(mFlexLinesResult, freeSpace);
                break;
            case SPACE_BETWEEN:
                alignContentSpaceBetween(mFlexLinesResult, freeSpace);
                break;
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
    private void alignContentStretch(FlexLines mFlexLinesResult, int freeSpace) {
        if (freeSpace > 0) {
            int unitSpace = freeSpace / mFlexLinesResult.size();
            for (FlexLine flexLine : mFlexLinesResult.mFlexLines) {
                flexLine.mCrossSize += unitSpace;
            }
        }
    }
    private void alignContentCenter(FlexLines mFlexLinesResult, int freeSpace) {
        int unitSpace = freeSpace / 2;
        mFlexLinesResult.addTop(FlexLine.createDummyWithCrossSize(unitSpace));
        mFlexLinesResult.addBottom(FlexLine.createDummyWithCrossSize(unitSpace));
    }
    private void alignContentSpaceAround(FlexLines mFlexLinesResult, int freeSpace) {
        if (freeSpace > 0) {
            float unitSpace = (float) freeSpace / (mFlexLinesResult.size() * 2);
            mFlexLinesResult.insertAround(unitSpace);
        } else {
            alignContentCenter(mFlexLinesResult, freeSpace);
        }
    }

    int determineMainSize(MeasureRequest mainAxisMeasureRequest) {
        int largestFlexLineMainSize = flexLines.getLargestMainSize();
        int expectedMainSize = mainAxisMeasureRequest.intentSize();
        if (mainAxisMeasureRequest.isTight()) {
            return expectedMainSize;
        } else {
            return Math.min(largestFlexLineMainSize, expectedMainSize);
        }
    }

    void calculateFlexibleLength(int mainSize, MeasureRequest crossAxisMeasureRequest) {
        for (FlexLine flexLine : flexLines.mFlexLines) {
            if (flexLine.mMainSize < mainSize && flexLine.mAnyItemsHaveFlexGrow) {
                calculateFlexibleLength(flexLine, mainSize, crossAxisMeasureRequest);
            } else if (flexLine.mMainSize > mainSize && flexLine.mAnyItemsHaveFlexShrink) {
                calculateFlexibleLength(flexLine, mainSize, crossAxisMeasureRequest);
            }
        }
    }

    private void calculateFlexibleLength(FlexLine flexLine, int containerMainSize,
                                         MeasureRequest crossAxisMeasureRequest) {
        RoundingErrorAccumulator errorAccumulator = new RoundingErrorAccumulator();
        boolean isMainAxisHorizontal = isMainAxisHorizontal();
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
                item.fixedMainSizeMeasure(roundedNewMainSize, crossAxisMeasureRequest,
                        flexLine.mSumCrossSizeBefore, isMainAxisHorizontal);
                item.clampByMinMaxCrossSize();
                flexLine.mMainSize += item.getOuterMainSize(isMainAxisHorizontal);
            }
            if (!hasViolation) {
                break;
            }
        }
        flexLine.refreshCrossSize(isMainAxisHorizontal);
    }
    List<FlexLine> fillFlexLines(MeasureRequest mainAxisMeasureRequest,
                                 MeasureRequest crossAxisMeasureRequest) {
        boolean isMainAxisHorizontal = isMainAxisHorizontal();
        // prepare flex lines
        List<FlexLine> flexLines = new ArrayList<>();
        // prepare current flex line
        FlexLine currentFlexLine = new FlexLine(paddings.getMainPaddings(isMainAxisHorizontal));

        int occupiedContainerCrossSize = paddings.getCrossPaddings(isMainAxisHorizontal);
        for (NewFlexItem item : items) {
            // measure flex item
            int occupiedMainSize = paddings.getMainPaddings(isMainAxisHorizontal);
            item.measure(mainAxisMeasureRequest, occupiedMainSize, crossAxisMeasureRequest,
                    occupiedContainerCrossSize, isMainAxisHorizontal);
            // clamp by min/max constraints and remeasure if needed
            item.clampByMinMaxCrossSize();
            if (isWrapNeeded(mainAxisMeasureRequest, flexProperties.flexWrap, currentFlexLine, item,
                    isMainAxisHorizontal)) {
                // finish current flex line
                currentFlexLine.mSumCrossSizeBefore = occupiedContainerCrossSize;
                flexLines.add(currentFlexLine);
                occupiedContainerCrossSize += currentFlexLine.mCrossSize;
                // remeasure if cross size MATCH_PARENT
                if (item.requireCrossSizeMatchParent(isMainAxisHorizontal)) {
                    item.measure(mainAxisMeasureRequest, occupiedMainSize,
                            crossAxisMeasureRequest, occupiedContainerCrossSize, isMainAxisHorizontal);
                }
                // prepare new flex line
                currentFlexLine = new FlexLine(paddings.getMainPaddings(isMainAxisHorizontal));
            }
            // add current item
            currentFlexLine.addItem(item, isMainAxisHorizontal);
        }
        flexLines.add(currentFlexLine);
        return flexLines;
    }

    private boolean isWrapNeeded(MeasureRequest mainAxisMeasureRequest,
                                 FlexWrap flexWrap,
                                 FlexLine currentFlexLine, NewFlexItem item,
                                 boolean isMainAxisHorizontal) {
        if (flexWrap == FlexWrap.NOWRAP) {
            return false;
        }
        if (mainAxisMeasureRequest.isUnconstrainted()) {
            return false;
        }
        return mainAxisMeasureRequest.intentSize()
                < currentFlexLine.mMainSize + item.getOuterMainSize(isMainAxisHorizontal);
    }

    @Override
    public void layout() {

    }

    public boolean isMainAxisHorizontal() {
        return flexProperties.flexDirection == FlexDirection.ROW || flexProperties.flexDirection == FlexDirection.ROW_REVERSE;
    }
}
