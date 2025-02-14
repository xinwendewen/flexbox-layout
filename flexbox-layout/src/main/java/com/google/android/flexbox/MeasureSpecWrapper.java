package com.google.android.flexbox;

import android.view.View;

import com.xinwendewen.flexbox.MeasureRequest;

class MeasureSpecWrapper implements MeasureRequest {
    int measureSpec;

    private static int getMeasureSpecMode(int measureSpec) {
        return View.MeasureSpec.getMode(measureSpec);
    }

    private static int getMeasureSpecSize(int measureSpec) {
        return View.MeasureSpec.getSize(measureSpec);
    }

    @Override
    public boolean isTight() {
        return getMeasureSpecMode(measureSpec) == View.MeasureSpec.EXACTLY;
    }

    @Override
    public boolean isUnconstrained() {
        return getMeasureSpecMode(measureSpec) == View.MeasureSpec.UNSPECIFIED;
    }

    @Override
    public int getExpectedSize() {
        return getMeasureSpecSize(measureSpec);
    }
}
