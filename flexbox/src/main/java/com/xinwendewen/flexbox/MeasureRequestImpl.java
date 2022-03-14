package com.xinwendewen.flexbox;

import android.view.View;

public class MeasureRequestImpl implements MeasureRequest {
    private final int measureSpec;

    public MeasureRequestImpl(int measureSpec) {
        this.measureSpec = measureSpec;
    }

    public static MeasureRequest createFrom(int measureSpec) {
       return new MeasureRequestImpl(measureSpec);
    }

    @Override
    public boolean isTight() {
        return isTight(measureSpec);
    }

    @Override
    public int intentSize() {
        return getMeasureSpecSize(measureSpec);
    }

    public static boolean isTight(int measureSpec) {
        return getMeasureSpecMode(measureSpec) == View.MeasureSpec.EXACTLY;
    }

    public static int getMeasureSpecMode(int measureSpec) {
        return View.MeasureSpec.getMode(measureSpec);
    }

    public static int getMeasureSpecSize(int measureSpec) {
        return View.MeasureSpec.getSize(measureSpec);
    }
}
