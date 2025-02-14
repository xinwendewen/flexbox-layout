package com.xinwendewen.flexbox;

public class RoundingErrorAccumulator {
    private double currentError;

    public int compensate() {
        if (currentError >= 1.0) {
            currentError -= 1.0;
            return 1;
        }
        if (currentError <= -1.0) {
            currentError += 1.0;
            return -1;
        }
        return 0;
    }

    public int round(double value) {
        int rounded = (int) Math.round(value);
        currentError += value - rounded;
        return rounded;
    }

    public int roundAndCompensate(double value) {
        int rounded = round(value);
        return rounded + compensate();
    }
}
