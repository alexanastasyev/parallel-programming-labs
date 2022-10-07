package org.example;

import org.example.integration.IntegralSolver;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Runner {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            double result = solveIntegralFromTask();
            long finishTime = System.currentTimeMillis();

            System.out.println("Result: " + result);
            System.out.println("Time: " + (finishTime - startTime) + " ms");
        });
        thread.start();
    }

    private static double solveIntegralFromTask() {
        Consumer<Integer> callback = (progress) -> System.out.println(progress + " %");

        IntegralSolver integralSolver = new IntegralSolver.Builder()
                .function(x -> Math.sin(x) * x)
                .lowerLimit(0)
                .upperLimit(1)
                .exactNumbers(8)
                .progressCallback(callback)
                .build();

        return integralSolver.solveIntegral();
    }
}
