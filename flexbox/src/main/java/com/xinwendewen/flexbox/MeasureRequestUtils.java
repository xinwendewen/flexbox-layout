package com.xinwendewen.flexbox;

import android.view.View;

public class MeasureRequestUtils {
    public static int getMeasureSpecMode(int measureSpec) {
        return View.MeasureSpec.getMode(measureSpec);
    }

    public static int getMeasureSpecSize(int measureSpec) {
        return View.MeasureSpec.getSize(measureSpec);
    }

    public static boolean isTight(int measureSpec) {
        return getMeasureSpecMode(measureSpec) == View.MeasureSpec.EXACTLY;
    }

    public static int generateExactlyMeasureSpec(int size) {
        return View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY);
    }
}
