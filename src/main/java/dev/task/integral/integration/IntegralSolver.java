package dev.task.integral.integration;

import java.util.function.Consumer;
import java.util.function.Function;

public class IntegralSolver {

    private Function<Double, Double> function;
    private double lowerLimit;
    private double upperLimit;
    private int exactNumbers;
    private Consumer<Integer> progressCallback = progress -> {
    };

    public double solveIntegral() {
//        throw new RuntimeException();
        if (lowerLimit > upperLimit) {
            throw new InvalidLimitsException("Lower limit should lower then upper limit");
        }
        if (lowerLimit == upperLimit) {
            return 0;
        }
        int segmentsNumber = calculateSegmentsNumber(lowerLimit, upperLimit, exactNumbers);
        double increment = (upperLimit - lowerLimit) / segmentsNumber;
        double accumulator = 0;
        long currentIteration = 0;
        int iterationIncrement = segmentsNumber / 20;
        for (double i = lowerLimit; i <= (upperLimit - increment); i += increment) {
            accumulator += increment * function.apply((i + i + increment) / 2);
            if (currentIteration % iterationIncrement == 0) {
                int progress = (int) ((currentIteration * 100) / segmentsNumber);
                progressCallback.accept(progress);
            }
            currentIteration++;
        }
        return accumulator;
    }

    private int calculateSegmentsNumber(double lowerLimit, double upperLimit, int exactNumbers) {
        double delta = 1.0 / Math.pow(10, exactNumbers);
        int segmentsNumber = 1;
        while ((upperLimit - lowerLimit) / segmentsNumber > delta) {
            segmentsNumber++;
        }
        return segmentsNumber;
    }

    @SuppressWarnings("unused")
    public static class Builder {
        private final IntegralSolver integralSolver = new IntegralSolver();

        public IntegralSolver.Builder function(Function<Double, Double> function) {
            integralSolver.function = function;
            return this;
        }

        public IntegralSolver.Builder lowerLimit(double lowerLimit) {
            integralSolver.lowerLimit = lowerLimit;
            return this;
        }

        public IntegralSolver.Builder upperLimit(double upperLimit) {
            integralSolver.upperLimit = upperLimit;
            return this;
        }

        public IntegralSolver.Builder exactNumbers(int exactNumbers) {
            integralSolver.exactNumbers = exactNumbers;
            return this;
        }

        public IntegralSolver.Builder progressCallback(Consumer<Integer> progressCallback) {
            integralSolver.progressCallback = progressCallback;
            return this;
        }

        public IntegralSolver build() {
            if (integralSolver.function == null) {
                throw new IllegalArgumentException("You must provide a function to solve integral");
            }
            return integralSolver;
        }
    }

}
