package dev.task.integral;

import dev.task.integral.concurrent.ConcurrentTaskIntegralSolver;

public class Runner {
    public static void main(String[] args) {
        ConcurrentTaskIntegralSolver solver = new ConcurrentTaskIntegralSolver();

        long start = System.currentTimeMillis();
        double result = solver.solve(12, (progress, total) -> System.out.println("Progress: " + progress + " / " + total));
        long finish = System.currentTimeMillis();

        System.out.println("Result: " + result);
        System.out.println("Time: " + (finish - start) + " ms");
    }
}
