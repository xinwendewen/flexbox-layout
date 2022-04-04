package com.xinwendewen.flexbox;

import java.util.ArrayList;
import java.util.List;

class FlexLines {
    List<FlexLine> flexLineList;

    void reset() {
        flexLineList = null;
    }

    int getLargestMainSize() {
        int largestMainSize = 0;
        for (FlexLine flexLine : flexLineList) {
            largestMainSize = Math.max(largestMainSize, flexLine.mainSize);
        }
        return largestMainSize;
    }

    boolean isSingleLine() {
        return flexLineList.size() == 1;
    }

    int getCrossSize() {
        int crossSize = 0;
        for (FlexLine flexLine : flexLineList) {
            crossSize += flexLine.crossSize;
        }
        return crossSize;
    }

    void addTop(FlexLine flexLine) {
        flexLineList.add(0, flexLine);
    }

    int size() {
        return flexLineList.size();
    }

    void addBottom(FlexLine flexLine) {
        flexLineList.add(flexLineList.size(), flexLine);
    }

    void insertBetweenFlexLines(float unitSpace) {
        RoundingErrorAccumulator errorAccumulator = new RoundingErrorAccumulator();
        List<FlexLine> newFlexLines = new ArrayList<>();
        for (int i = 0; i < flexLineList.size(); i++) {
            FlexLine flexLine = flexLineList.get(i);
            if (i != 0) {
                FlexLine dummyFlexLine =
                        FlexLine.createDummyWithCrossSize(errorAccumulator.roundAndCompensate(unitSpace));
                newFlexLines.add(dummyFlexLine);
            }
            newFlexLines.add(flexLine);
        }
        flexLineList = newFlexLines;
    }

    void insertAround(float space) {
        RoundingErrorAccumulator errorAccumulator = new RoundingErrorAccumulator();
        List<FlexLine> newFlexLines = new ArrayList<>();
        for (FlexLine currentFlexLine : flexLineList) {
            FlexLine dummyFlexLine =
                    FlexLine.createDummyWithCrossSize(errorAccumulator.roundAndCompensate(space));
            newFlexLines.add(dummyFlexLine);
            newFlexLines.add(currentFlexLine);
            dummyFlexLine =
                    FlexLine.createDummyWithCrossSize(errorAccumulator.roundAndCompensate(space));
            newFlexLines.add(dummyFlexLine);
        }
        flexLineList = newFlexLines;
    }
}
