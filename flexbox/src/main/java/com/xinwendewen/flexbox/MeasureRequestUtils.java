package com.xinwendewen.flexbox;

import android.view.View;

public class MeasureRequestUtils {
    public static int getMeasureSpecMode(int measureSpec) {
        return View.MeasureSpec.getMode(measureSpec);
    }

    public static int getMeasureSpecSize(int measureSpec) {
        return View.MeasureSpec.getSize(measureSpec);
    }

    public static boolean isUnspecifiedMode(int measureSpec) {
        return getMeasureSpecMode(measureSpec) == View.MeasureSpec.UNSPECIFIED;
    }
    public static boolean isExactliyMode(int measureSpec) {
        return getMeasureSpecMode(measureSpec) == View.MeasureSpec.EXACTLY;
    }

    public static int combineMeasureStates(int currentState, int newState) {
        return currentState | newState;
    }
    public static int generateExactlyMeasureSpec(int size) {
        return View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY);
    }

    public static int generateMeasureSpec(int size, int mode) {
        return View.MeasureSpec.makeMeasureSpec(size, mode);
    }
}
