package com.xinwendewen.flexbox;

import com.google.android.flexbox.FlexContainer;
import com.google.android.flexbox.FlexDirection;

public class ContainerProperties {
    final FlexContainer container;
    final int flexDirection;
    final int justifyContent;
    final int flexWrap;
    final int alignContent;
    final int alignItems;
    final boolean isMainAxisHorizontal;

    public ContainerProperties(FlexContainer container) {
        this.container = container;
        this.flexDirection = container.getFlexDirection();
        this.justifyContent = container.getJustifyContent();
        this.flexWrap = container.getFlexWrap();
        this.alignContent = container.getAlignContent();
        this.alignItems = container.getAlignItems();
        this.isMainAxisHorizontal = isMainAxisHorizontal(flexDirection);
    }

    public int getFlexDirection() {
        return flexDirection;
    }

    public int getJustifyContent() {
        return justifyContent;
    }

    public int getFlexWrap() {
        return flexWrap;
    }

    public int getAlignContent() {
        return alignContent;
    }

    public int getAlignItems() {
        return alignItems;
    }

    public boolean isMainAxisHorizontal() {
        return isMainAxisHorizontal;
    }
    public static boolean isMainAxisHorizontal(int flexDirection) {
        return flexDirection == FlexDirection.ROW || flexDirection == FlexDirection.ROW_REVERSE;
    }

    public int getMainPaddings() {
        int mainPaddingStart = getMainPaddingStart(isMainAxisHorizontal);
        int mainPaddingEnd = getMainPaddingEnd(isMainAxisHorizontal);
        return mainPaddingStart + mainPaddingEnd;
    }

    public int getCrossPaddings() {
        int crossPaddingStart = getCrossPaddingStart();
        int crossPaddingEnd = getCrossPaddingEnd();
        return crossPaddingStart + crossPaddingEnd;
    }

    int getCrossPaddingStart() {
        if (isMainAxisHorizontal) {
            return container.getPaddingTop();
        } else {
            return container.getPaddingStart();
        }
    }

    int getCrossPaddingEnd() {
        if (isMainAxisHorizontal) {
            return container.getPaddingBottom();
        } else {
            return container.getPaddingEnd();
        }
    }
    private int getMainPaddingStart(boolean isMainHorizontal) {
        if (isMainHorizontal) {
            return container.getPaddingStart();
        }
        return container.getPaddingTop();
    }

    private int getMainPaddingEnd(boolean isMainHorizontal) {
        if (isMainHorizontal) {
            return container.getPaddingEnd();
        } else {
            return container.getPaddingBottom();
        }
    }
}
