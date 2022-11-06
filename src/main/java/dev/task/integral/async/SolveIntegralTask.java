package dev.task.integral.async;

import dev.task.concurrent.semaphore.SimpleSemaphore;
import dev.task.integral.integration.IntegralSolver;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

public class SolveIntegralTask implements Callable<Double> {

    private static final int EXACT_NUMBERS = 9;
    private final double lowerLimit;
    private final double upperLimit;
    private final Function<Double, Double> function;
    private final Consumer<Boolean> onFinish;
    private final SimpleSemaphore semaphore;

    public SolveIntegralTask(double lowerLimit, double upperLimit, Function<Double, Double> function, Consumer<Boolean> onFinish, SimpleSemaphore semaphore) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.function = function;
        this.onFinish = onFinish;
        this.semaphore = semaphore;
    }

    @Override
    public Double call() throws Exception {
        semaphore.acquire();

        IntegralSolver integralSolver = new IntegralSolver.Builder()
                .function(function)
                .lowerLimit(lowerLimit)
                .upperLimit(upperLimit)
                .exactNumbers(EXACT_NUMBERS)
                .build();

        double result = integralSolver.solveIntegral();
        onFinish.accept(true);

        semaphore.release();

        return result;
    }
}
