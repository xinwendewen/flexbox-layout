package com.xinwendewen.flexbox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlexLine {
    final List<FlexItem> items = new ArrayList<>();

    int mainSize;

    int crossSize;

    float totalFlexGrow;

    float totalFlexShrink;

    int crossSizeSumAbove;

    boolean hasFlexibleItem;

    Set<Integer> violatedIndices = new HashSet<>();

    static FlexLine createDummyWithCrossSize(int crossSize) {
        FlexLine flexLine = new FlexLine();
        flexLine.crossSize = crossSize;
        return flexLine;
    }

    FlexItem getItemAt(int index) {
        return items.get(index);
    }

    void addItem(FlexItem item, boolean isMainAxisHorizontal) {
        items.add(item);
        hasFlexibleItem |= item.getFlexGrow() > 0;
        hasFlexibleItem |= item.getFlexShrink() > 0;
        mainSize += item.getOuterMainSize(isMainAxisHorizontal);
        totalFlexGrow += item.getFlexGrow();
        totalFlexShrink += item.getFlexShrink();
        crossSize = Math.max(crossSize, item.getOuterCrossSize(isMainAxisHorizontal));
    }

    public int getMainSize() {
        return mainSize;
    }

    public int getCrossSize() {
        return crossSize;
    }

    public int getItemCount() {
        return items.size();
    }

    boolean isFrozen() {
        for (int i = 0; i < items.size(); i++) {
            FlexItem item = items.get(i);
            boolean isItemFrozen = !item.isFlexible() || violatedIndices.contains(i);
            if (!isItemFrozen) {
                return false;
            }
        }
        return true;
    }

    boolean isItemShrinkFrozen(int index) {
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

    boolean isItemGrowFrozen(int index) {
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

    void freezeItemAt(int index) {
        violatedIndices.add(index);
        FlexItem item = items.get(index);
        float growFactor = item.getFlexGrow();
        if (growFactor > 0) {
            totalFlexGrow -= growFactor;
        }
        float shrinkFactor = item.getFlexShrink();
        if (shrinkFactor > 0) {
            totalFlexShrink -= shrinkFactor;
        }
    }

    void refreshCrossSize(boolean isMainAxisHorizontal) {
        int crossSize = 0;
        for (FlexItem item : items) {
            crossSize = Math.max(crossSize, item.getOuterCrossSize(isMainAxisHorizontal));
        }
    }
}
