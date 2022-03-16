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

    public double currentError() {
        return currentError;
    }

    public double getAndReset() {
        double value = currentError;
        currentError = 0;
        return value;
    }

    public int round(double value) {
        int rounded = (int) Math.round(value);
        currentError += value - rounded;
        return rounded;
    }

    public void accumulatedError(double error) {
        currentError += error;
    }
}
