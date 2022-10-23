package dev.task.integral.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConcurrentTaskIntegralSolver {

    private static final double LOWER_LIMIT = 0;
    private static final double UPPER_LIMIT = 1;
    private static final int TASK_WAITING_TIME = 10;
    private static final int MULTY_THREADS_AMOUNT = 3;
    private static final Function<Double, Double> FUNCTION = x -> Math.sin(x) * x;

    public double solve(int threadsNum, BiConsumer<Integer, Integer> progressCallback) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadsNum);
        double step = (UPPER_LIMIT - LOWER_LIMIT) / threadsNum;
        double lowerLimit = LOWER_LIMIT;
        double upperLimit = lowerLimit + step;
        List<Future<Double>> futures = new ArrayList<>();

        AtomicInteger progress = new AtomicInteger(0);
        progressCallback.accept(progress.get(), threadsNum);
        ReentrantLock progressLock = new ReentrantLock();

        List<Long> tasksFinishTimestamps = new ArrayList<>();
        CountDownLatch allTasksFinishLatch = new CountDownLatch(threadsNum);

        Consumer<Boolean> onTaskFinish = (Boolean finished) -> {
            allTasksFinishLatch.countDown();
            tasksFinishTimestamps.add(System.currentTimeMillis());

            progressLock.lock();

            progress.set(progress.get() + 1);
            progressCallback.accept(progress.get(), threadsNum);

            progressLock.unlock();
        };

        Semaphore semaphore = new Semaphore(MULTY_THREADS_AMOUNT);
        for (int i = 0; i < threadsNum; i++) {
            double currentLowerLimit = lowerLimit;
            double currentUpperLimit = upperLimit;
            Future<Double> future = executorService.submit(
                    new SolveIntegralTask(currentLowerLimit, currentUpperLimit, FUNCTION, onTaskFinish, semaphore)
            );
            futures.add(future);
            lowerLimit = upperLimit;
            upperLimit += step;
        }

        List<Long> taskDelays = new ArrayList<>();
        try {
            //noinspection ResultOfMethodCallIgnored
            allTasksFinishLatch.await(TASK_WAITING_TIME, TimeUnit.MINUTES);
            long totalFinishTime = System.currentTimeMillis();
            tasksFinishTimestamps.forEach(time -> taskDelays.add(totalFinishTime - time));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        taskDelays.forEach(taskDelay -> System.out.println(taskDelay + " ms"));

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
