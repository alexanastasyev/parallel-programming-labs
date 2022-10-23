package dev.task.integral.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ConcurrentTaskIntegralSolver {

    private static final double LOWER_LIMIT = 0;
    private static final double UPPER_LIMIT = 1;
    private static final int TASK_WAITING_TIME = 10;

    public double solve(int threadsNum, BiConsumer<Integer, Integer> progressCallback) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadsNum);
        double step = (UPPER_LIMIT - LOWER_LIMIT) / threadsNum;
        double lowerLimit = LOWER_LIMIT;
        double upperLimit = lowerLimit + step;
        List<Future<Double>> futures = new ArrayList<>();

        AtomicInteger progress = new AtomicInteger(0);
        progressCallback.accept(progress.get(), threadsNum);

        Consumer<Boolean> onTaskFinish = (Boolean finished) -> {
            progress.set(progress.get() + 1);
            progressCallback.accept(progress.get(), threadsNum);
        };

        for (int i = 0; i < threadsNum; i++) {
            double currentLowerLimit = lowerLimit;
            double currentUpperLimit = upperLimit;
            Future<Double> future = executorService.submit(
                    new SolveIntegralTask(currentLowerLimit, currentUpperLimit, x -> Math.sin(x) * x, onTaskFinish)
            );
            futures.add(future);
            lowerLimit = upperLimit;
            upperLimit += step;
        }

        AtomicReference<Double> result = new AtomicReference<>((double) 0);
        futures.forEach(future -> {
            try {
                double value = future.get(TASK_WAITING_TIME, TimeUnit.MINUTES);
                double previous = result.get();
                result.set(previous + value);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                System.out.println(e.getMessage());
            }
        });

        executorService.shutdown();
        return result.get();
    }
}
