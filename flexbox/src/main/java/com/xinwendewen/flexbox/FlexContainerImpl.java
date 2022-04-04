package com.xinwendewen.flexbox;

import static com.xinwendewen.flexbox.AlignSelf.AUTO;
import static com.xinwendewen.flexbox.AlignSelf.STRETCH;
import static com.xinwendewen.flexbox.FlexWrap.WRAP;
import static com.xinwendewen.flexbox.FlexWrap.WRAP_REVERSE;

import java.util.ArrayList;
import java.util.List;

public class FlexContainerImpl implements FlexContainer {
    private final List<FlexItem> items = new ArrayList<>();
    private final FlexLines flexLines = new FlexLines();
    private final ContainerFlexProperties flexProperties = new ContainerFlexProperties();
    private final Paddings paddings = new Paddings();

    @Override
    public List<FlexLine> getFlexLines() {
        return flexLines.flexLineList;
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
    public <T extends FlexItem> void setFlexItems(List<T> flexItems, int count) {
        items.clear();
        for (int i = 0; i < count; i++) {
            items.add(flexItems.get(i));
        }
    }

    @Override
    public void measure(MeasureRequest mainAxisMeasureRequest, MeasureRequest crossAxisMeasureRequest) {
        flexLines.reset();
        flexLines.flexLineList = fillFlexLines(mainAxisMeasureRequest, crossAxisMeasureRequest);
        int mainSize = determineMainSize(mainAxisMeasureRequest);
        calculateFlexibleLength(mainSize, crossAxisMeasureRequest);
        if (crossAxisMeasureRequest.isTight()) {
            if (flexLines.isSingleLine()) {
                flexLines.flexLineList.get(0).crossSize =
                        crossAxisMeasureRequest.getExpectedSize() - paddings.getCrossPaddings(isMainAxisHorizontal());
            } else {
                int determinedCrossSize = crossAxisMeasureRequest.getExpectedSize();
                int containerCrossAxisPadding = paddings.getCrossPaddings(isMainAxisHorizontal());
                crossAlignment(determinedCrossSize - containerCrossAxisPadding, flexLines);
            }
        }
        stretchItems();
    }

    void stretchItems() {
        for (FlexLine flexLine : flexLines.flexLineList) {
            for (FlexItem item : flexLine.items) {
                if (needStretch(item, flexProperties.alignItems, flexLine.crossSize,
                        isMainAxisHorizontal())) {
                    stretchItem(item, flexLine, isMainAxisHorizontal());
                }
            }
        }
    }

    private void stretchItem(FlexItem item, FlexLine flexLine, boolean isMainAxisHorizontal) {
        int newCrossSize = flexLine.crossSize - item.getCrossAxisMargin(isMainAxisHorizontal);
        newCrossSize = item.getClampedCrossSize(newCrossSize, isMainAxisHorizontal);
        item.fixedSizeMeasure(item.getMeasuredMainSize(isMainAxisHorizontal), newCrossSize, isMainAxisHorizontal);
    }

    private boolean needStretch(FlexItem item, AlignItems alignItems, int flexLineCrossSize,
                                boolean isMainAxisHorizontal) {
        if (item.getOuterCrossSize(isMainAxisHorizontal) >= flexLineCrossSize) {
            return false;
        }
        if (item.getAlignSelf() == STRETCH) {
            return true;
        }
        if (item.getAlignSelf() == AUTO && alignItems == AlignItems.STRETCH) {
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
            for (FlexLine flexLine : mFlexLinesResult.flexLineList) {
                flexLine.crossSize += unitSpace;
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
        int expectedMainSize = mainAxisMeasureRequest.getExpectedSize();
        if (mainAxisMeasureRequest.isTight()) {
            return expectedMainSize;
        } else {
            return Math.min(largestFlexLineMainSize, expectedMainSize);
        }
    }

    void calculateFlexibleLength(int mainSize, MeasureRequest crossAxisMeasureRequest) {
        for (FlexLine flexLine : flexLines.flexLineList) {
            if (flexLine.hasFlexibleItem &&
                    (flexLine.mainSize != mainSize)) {
                calculateFlexibleLength(flexLine, mainSize, crossAxisMeasureRequest);
            }
        }
    }

    private void calculateFlexibleLength(FlexLine flexLine, int containerMainSize,
                                         MeasureRequest crossAxisMeasureRequest) {
        RoundingErrorAccumulator errorAccumulator = new RoundingErrorAccumulator();
        boolean isMainAxisHorizontal = isMainAxisHorizontal();
        while (!flexLine.isFrozen() && flexLine.mainSize != containerMainSize) {
            int available = containerMainSize - flexLine.mainSize;
            float spaceUnit = available / (available > 0 ? flexLine.totalFlexGrow : flexLine.totalFlexShrink);
            boolean hasViolation = false;
            for (int i = 0; i < flexLine.getItemCount(); i++) {
                FlexItem item = flexLine.getItemAt(i);
                if (available < 0 && flexLine.isItemShrinkFrozen(i)) {
                    continue;
                }
                if (available > 0 && flexLine.isItemGrowFrozen(i)) {
                    continue;
                }
                flexLine.mainSize -= item.getOuterMainSize(isMainAxisHorizontal);
                int measuredMainSize =
                        item.getMeasuredMainSize(isMainAxisHorizontal);
                float newMainSize = measuredMainSize + spaceUnit * (available > 0 ?
                        item.getFlexGrow() : item.getFlexShrink());
                if (newMainSize < item.getMinMainSize(isMainAxisHorizontal)) {
                    hasViolation = true;
                    newMainSize = item.getMinMainSize(isMainAxisHorizontal);
                    flexLine.freezeItemAt(i);
                } else if (newMainSize > item.getMaxMainSize(isMainAxisHorizontal)) {
                    hasViolation = true;
                    newMainSize = item.getMaxMainSize(isMainAxisHorizontal);
                    flexLine.freezeItemAt(i);
                }
                int roundedNewMainSize = errorAccumulator.round(newMainSize);
                roundedNewMainSize += errorAccumulator.compensate();
                item.fixedMainSizeMeasure(roundedNewMainSize, crossAxisMeasureRequest,
                        flexLine.crossSizeSumAbove, isMainAxisHorizontal);
                item.clampByMinMaxDimensions();
                flexLine.mainSize += item.getOuterMainSize(isMainAxisHorizontal);
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
        FlexLine currentFlexLine = new FlexLine();

        int occupiedContainerCrossSize = paddings.getCrossPaddings(isMainAxisHorizontal);
        for (FlexItem item : items) {
            // measure flex item
            int occupiedMainSize = paddings.getMainPaddings(isMainAxisHorizontal);
            item.measure(mainAxisMeasureRequest, occupiedMainSize, crossAxisMeasureRequest,
                    occupiedContainerCrossSize, isMainAxisHorizontal);
            // clamp by min/max constraints and remeasure if needed
            item.clampByMinMaxDimensions();
            if (isWrapNeeded(mainAxisMeasureRequest, flexProperties.flexWrap, currentFlexLine, item,
                    isMainAxisHorizontal)) {
                // finish current flex line
                currentFlexLine.crossSizeSumAbove = occupiedContainerCrossSize;
                flexLines.add(currentFlexLine);
                occupiedContainerCrossSize += currentFlexLine.crossSize;
                // remeasure if cross size MATCH_PARENT
                if (item.requireCrossSizeMatchParent(isMainAxisHorizontal)) {
                    item.measure(mainAxisMeasureRequest, occupiedMainSize,
                            crossAxisMeasureRequest, occupiedContainerCrossSize, isMainAxisHorizontal);
                }
                // prepare new flex line
                currentFlexLine = new FlexLine();
            }
            // add current item
            currentFlexLine.addItem(item, isMainAxisHorizontal);
        }
        flexLines.add(currentFlexLine);
        return flexLines;
    }

    private boolean isWrapNeeded(MeasureRequest mainAxisMeasureRequest,
                                 FlexWrap flexWrap,
                                 FlexLine currentFlexLine, FlexItem item,
                                 boolean isMainAxisHorizontal) {
        if (flexWrap == FlexWrap.NOWRAP) {
            return false;
        }
        if (mainAxisMeasureRequest.isUnconstrained()) {
            return false;
        }
        return mainAxisMeasureRequest.getExpectedSize()
                < currentFlexLine.mainSize + item.getOuterMainSize(isMainAxisHorizontal);
    }

    @Override
    public void layout(int left, int top, int right, int bottom, boolean isRtl) {
        layout(left, top, right, bottom, isRtl, paddings);
    }

    private void layout(int left, int top, int right, int bottom, boolean isRtl, Paddings paddings) {
        int width = right - left;
        int innerWidth = width - paddings.startPadding - paddings.endPadding;
        int height = bottom - top;
        int innerHeight = height - paddings.topPadding - paddings.bottomPadding;
        int containerInnerMainSize;
        int containerInnerCrossSize;
        boolean isMainAxisHorizontal = isMainAxisHorizontal();
        if (isMainAxisHorizontal) {
            containerInnerMainSize = innerWidth;
            containerInnerCrossSize = innerHeight;
        } else {
            containerInnerMainSize = innerHeight;
            containerInnerCrossSize = innerWidth;
        }
        boolean isMainAxisReversed = false;
        boolean isCrossAxisReversed = false;
        FlexDirection flexDirection = flexProperties.flexDirection;
        FlexWrap flexWrap = flexProperties.flexWrap;
        switch (flexDirection) {
            case ROW:
                if (isRtl) {
                    isMainAxisReversed = true;
                }
                if (flexWrap == WRAP_REVERSE) {
                    isCrossAxisReversed = true;
                }
                break;
            case ROW_REVERSE:
                if (!isRtl) {
                    isMainAxisReversed = true;
                }
                if (flexWrap == WRAP_REVERSE) {
                    isCrossAxisReversed = true;
                }
                break;
            case COLUMN:
                if (!isRtl && flexWrap == WRAP_REVERSE) {
                    isCrossAxisReversed = true;
                }
                if (isRtl && flexWrap == WRAP) {
                    isCrossAxisReversed = true;
                }
                break;
            case COLUMN_REVERSE:
                if (!isRtl && flexWrap == WRAP_REVERSE) {
                    isCrossAxisReversed = true;
                }
                if (isRtl && flexWrap == WRAP) {
                    isCrossAxisReversed = true;
                }
                isMainAxisReversed = true;
        }
        int crossAxisAnchor = isCrossAxisReversed ? containerInnerCrossSize : 0;
        for (FlexLine flexLine : flexLines.flexLineList) {
            JustifyContent justifyContent = flexProperties.justifyContent;
            int flexLineMainSize = flexLine.mainSize;
            int mainAxisAnchor = 0;
            float spaceBetweenItems = 0;
            switch (justifyContent) {
                case FLEX_START:
                    mainAxisAnchor = isMainAxisReversed ? containerInnerMainSize : 0;
                    break;
                case FLEX_END:
                    mainAxisAnchor = isMainAxisReversed ? flexLineMainSize :
                            containerInnerMainSize - flexLineMainSize;
                    break;
                case CENTER:
                    mainAxisAnchor = isMainAxisReversed ?
                            (containerInnerMainSize + flexLineMainSize) / 2 :
                            (containerInnerMainSize - flexLineMainSize) / 2;
                    break;
                case SPACE_AROUND:
                    spaceBetweenItems =
                            (float) (containerInnerMainSize - flexLineMainSize) / flexLine.getItemCount();
                    mainAxisAnchor = isMainAxisReversed ?
                            containerInnerMainSize - Math.round(spaceBetweenItems) / 2 :
                            Math.round(spaceBetweenItems) / 2;
                    break;
                case SPACE_BETWEEN:
                    spaceBetweenItems = flexLine.getItemCount() > 1 ?
                            (float) (containerInnerMainSize - flexLineMainSize) / (flexLine.getItemCount() - 1) : 0;
                    mainAxisAnchor = isMainAxisReversed ? containerInnerMainSize : 0;
                    break;
                case SPACE_EVENLY:
                    spaceBetweenItems =
                            (float) (containerInnerMainSize - flexLineMainSize) / (flexLine.getItemCount() + 1);
                    mainAxisAnchor = isMainAxisReversed ?
                            Math.round(containerInnerMainSize - spaceBetweenItems) :
                            Math.round(spaceBetweenItems);
                    break;
            }
            RoundingErrorAccumulator errorAccumulator = new RoundingErrorAccumulator();
            for (int i = 0; i < flexLine.getItemCount(); i++) {
                FlexItem item = flexLine.getItemAt(i);
                layoutItem(item, isMainAxisReversed, mainAxisAnchor, isCrossAxisReversed,
                        crossAxisAnchor, isMainAxisHorizontal, flexProperties.alignItems,
                        flexLine, paddings.leftPadding, paddings.topPadding);
                mainAxisAnchor = forwardMainAxisAnchor(mainAxisAnchor, isMainAxisReversed, item,
                        errorAccumulator.roundAndCompensate(spaceBetweenItems),
                        isMainAxisHorizontal);
            }
            crossAxisAnchor = forwardCrossAxisAnchor(crossAxisAnchor, isCrossAxisReversed, flexLine);
        }
    }

    private int forwardMainAxisAnchor(int mainAxisAnchor, boolean isMainAxisReversed,
                                      FlexItem item, int spaceBetweenItems,
                                      boolean isMainAxisHorizontal) {
        return isMainAxisReversed ?
                mainAxisAnchor - (item.getMainAxisMarginStart(isMainAxisHorizontal) + item.getMeasuredMainSize(isMainAxisHorizontal) + spaceBetweenItems) :
                mainAxisAnchor + (item.getMainAxisMarginEnd(isMainAxisHorizontal) + item.getMeasuredMainSize(isMainAxisHorizontal) + spaceBetweenItems);
    }

    private int forwardCrossAxisAnchor(int crossAxisAnchor, boolean isCrossAxisReversed, FlexLine flexLine) {
        return isCrossAxisReversed ? crossAxisAnchor - flexLine.getCrossSize() : crossAxisAnchor + flexLine.getCrossSize();
    }

    void layoutItem(FlexItem item, boolean isMainAxisReversed, int mainAxisAnchor,
                    boolean isCrossAxisReversed, int crossAxisAnchor,
                    boolean isMainAxisHorizontal, AlignItems alignItems, FlexLine flexLine,
                    int leftPadding, int topPadding) {
        int mainStart;
        int mainEnd;
        int crossStart;
        int crossEnd;
        if (isMainAxisReversed) {
            mainEnd = mainAxisAnchor - item.getMainAxisMarginEnd(isMainAxisHorizontal);
            mainStart = mainEnd - item.getMeasuredMainSize(isMainAxisHorizontal);
        } else {
            mainStart = mainAxisAnchor + item.getMainAxisMarginStart(isMainAxisHorizontal);
            mainEnd = mainStart + item.getMeasuredMainSize(isMainAxisHorizontal);
        }
        AlignItems crossAlignment = alignItems;
        AlignSelf alignSelf = item.getAlignSelf();
        if (alignSelf != AUTO) {
            switch (alignSelf) {
                case FLEX_START:
                    crossAlignment = AlignItems.FLEX_START;
                    break;
                case FLEX_END:
                    crossAlignment = AlignItems.FLEX_END;
                    break;
                case CENTER:
                    crossAlignment = AlignItems.CENTER;
                    break;
                case STRETCH:
                    crossAlignment = AlignItems.STRETCH;
                    break;
            }
        }
        switch (crossAlignment) {
            case STRETCH:
            case FLEX_START:
                if (isCrossAxisReversed) {
                    crossEnd = crossAxisAnchor - item.getCrossAxisMarginEnd(isMainAxisHorizontal);
                    crossStart = crossEnd - item.getMeasuredCrossSize(isMainAxisHorizontal);
                } else {
                    crossStart = crossAxisAnchor + item.getCrossAxisMarginStart(isMainAxisHorizontal);
                    crossEnd = crossStart + item.getMeasuredCrossSize(isMainAxisHorizontal);
                }
                break;
            case FLEX_END:
                if (isCrossAxisReversed) {
                    crossStart =
                            crossAxisAnchor - flexLine.crossSize + item.getCrossAxisMarginStart(isMainAxisHorizontal);
                    crossEnd = crossStart + item.getMeasuredCrossSize(isMainAxisHorizontal);
                } else {
                    crossEnd =
                            crossAxisAnchor + flexLine.crossSize - item.getCrossAxisMarginEnd(isMainAxisHorizontal);
                    crossStart = crossEnd - item.getMeasuredCrossSize(isMainAxisHorizontal);
                }
                break;
            case CENTER:
                if (isCrossAxisReversed) {
                    crossEnd =
                            crossAxisAnchor - (flexLine.crossSize / 2 -
                                    item.getOuterCrossSize(isMainAxisHorizontal) / 2) -
                                    item.getCrossAxisMarginEnd(isMainAxisHorizontal);
                    crossStart = crossEnd - item.getMeasuredCrossSize(isMainAxisHorizontal);
                } else {
                    crossStart =
                            crossAxisAnchor + flexLine.crossSize / 2 -
                                    item.getOuterCrossSize(isMainAxisHorizontal) / 2 +
                                    item.getCrossAxisMarginStart(isMainAxisHorizontal);
                    crossEnd = crossStart + item.getMeasuredCrossSize(isMainAxisHorizontal);
                }
                break;
            default:
                throw new IllegalStateException();
        }
        item.layout(mainStart, mainEnd, crossStart, crossEnd, isMainAxisHorizontal, leftPadding,
                topPadding);
    }

    public boolean isMainAxisHorizontal() {
        return flexProperties.flexDirection == FlexDirection.ROW || flexProperties.flexDirection == FlexDirection.ROW_REVERSE;
    }
}
