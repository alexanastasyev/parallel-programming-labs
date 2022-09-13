package org.example;

import org.example.integration.IntegralSolver;

import java.util.function.Function;

public class Runner {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        double result = solveIntegralFromTask();
        long finishTime = System.currentTimeMillis();

        System.out.println("Result: " + result);
        System.out.println("Time: " + (finishTime - startTime) + " ms");
    }

    private static double solveIntegralFromTask() {
        Function<Double, Double> function = x -> Math.sin(x) * x;
        int lowerLimit = 0;
        int upperLimit = 1;
        int exactNumbers = 8;

        IntegralSolver integralSolver = new IntegralSolver();
        return integralSolver.solveIntegral(function, lowerLimit, upperLimit, exactNumbers);
    }
}
