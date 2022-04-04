package com.xinwendewen.flexbox;

import com.xinwendewen.flexbox.FlexLine;
import com.xinwendewen.flexbox.RoundingErrorAccumulator;

import java.util.ArrayList;
import java.util.List;

public class FlexLines {
    public List<FlexLine> mFlexLines;

    public void reset() {
        mFlexLines = null;
    }

    public int getLargestMainSize() {
        int largestMainSize = 0;
        for (FlexLine flexLine : mFlexLines) {
            largestMainSize = Math.max(largestMainSize, flexLine.mMainSize);
        }
        return largestMainSize;
    }

    public boolean isSingleLine() {
        return mFlexLines.size() == 1;
    }

    public int getCrossSize() {
        int crossSize = 0;
        for (FlexLine flexLine : mFlexLines) {
            crossSize += flexLine.mCrossSize;
        }
        return crossSize;
    }

    public void addTop(FlexLine flexLine) {
        mFlexLines.add(0, flexLine);
    }

    public int size() {
        return mFlexLines.size();
    }

    public void addBottom(FlexLine flexLine) {
        mFlexLines.add(mFlexLines.size(), flexLine);
    }

    public void insertBetweenFlexLines(float unitSpace) {
        RoundingErrorAccumulator errorAccumulator = new RoundingErrorAccumulator();
        List<FlexLine> newFlexLines = new ArrayList<>();
        for (int i = 0; i < mFlexLines.size(); i++) {
            FlexLine flexLine = mFlexLines.get(i);
            if (i != 0) {
                FlexLine dummyFlexLine =
                        FlexLine.createDummyWithCrossSize(errorAccumulator.roundAndCompensate(unitSpace));
                newFlexLines.add(dummyFlexLine);
            }
            newFlexLines.add(flexLine);
        }
        mFlexLines = newFlexLines;
    }

    public void insertAround(float space) {
        RoundingErrorAccumulator errorAccumulator = new RoundingErrorAccumulator();
        List<FlexLine> newFlexLines = new ArrayList<>();
        for (FlexLine currentFlexLine : mFlexLines) {
            FlexLine dummyFlexLine =
                    FlexLine.createDummyWithCrossSize(errorAccumulator.roundAndCompensate(space));
            newFlexLines.add(dummyFlexLine);
            newFlexLines.add(currentFlexLine);
            dummyFlexLine =
                    FlexLine.createDummyWithCrossSize(errorAccumulator.roundAndCompensate(space));
            newFlexLines.add(dummyFlexLine);
        }
        mFlexLines = newFlexLines;
    }
}
