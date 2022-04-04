package com.xinwendewen.flexbox;

public class Paddings {
    public int startPadding;
    public int endPadding;
    public int leftPadding;
    public int rightPadding;
    public int topPadding;
    public int bottomPadding;

    public void update(Paddings other) {
        startPadding = other.startPadding;
        endPadding = other.endPadding;
        leftPadding = other.leftPadding;
        rightPadding = other.rightPadding;
        topPadding = other.topPadding;
        bottomPadding = other.bottomPadding;
    }

    public int getMainPaddings(boolean isMainAxisHorizontal) {
        int mainPaddingStart = getMainPaddingStart(isMainAxisHorizontal);
        int mainPaddingEnd = getMainPaddingEnd(isMainAxisHorizontal);
        return mainPaddingStart + mainPaddingEnd;
    }

    private int getMainPaddingStart(boolean isMainHorizontal) {
        if (isMainHorizontal) {
            return startPadding;
        }
        return topPadding;
    }

    private int getMainPaddingEnd(boolean isMainHorizontal) {
        if (isMainHorizontal) {
            return endPadding;
        } else {
            return bottomPadding;
        }
    }

    public int getCrossPaddings(boolean isMainAxisHorizontal) {
        int crossPaddingStart = getCrossPaddingStart(isMainAxisHorizontal);
        int crossPaddingEnd = getCrossPaddingEnd(isMainAxisHorizontal);
        return crossPaddingStart + crossPaddingEnd;
    }

    int getCrossPaddingStart(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return topPadding;
        } else {
            return startPadding;
        }
    }

    int getCrossPaddingEnd(boolean isMainAxisHorizontal) {
        if (isMainAxisHorizontal) {
            return bottomPadding;
        } else {
            return endPadding;
        }
    }
}
