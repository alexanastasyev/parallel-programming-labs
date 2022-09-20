package org.example.integration;

import java.util.function.Consumer;
import java.util.function.Function;

public class IntegralSolver {

    public double solveIntegral(Function<Double, Double> function, double lowerLimit, double upperLimit, int exactNumbers) {
        return this.solveIntegral(function, lowerLimit, upperLimit, exactNumbers, progress -> {
        });
    }

    public double solveIntegral(Function<Double, Double> function, double lowerLimit, double upperLimit, int exactNumbers, Consumer<Byte> callback) {
        if (lowerLimit > upperLimit) {
            throw new InvalidLimitsException();
        }
        if (lowerLimit == upperLimit) {
            return 0;
        }
        int segmentsNumber = calculateSegmentsNumber(lowerLimit, upperLimit, exactNumbers);
        double increment = (upperLimit - lowerLimit) / segmentsNumber;
        double accumulator = 0;
        int currentIteration = 0;
        for (double i = lowerLimit; i <= (upperLimit - increment); i += increment) {
            accumulator += increment * function.apply((i + i + increment) / 2);
            byte progress = (byte) Math.round( (((float) currentIteration) / segmentsNumber) * 100 );
            callback.accept(progress);
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

}
