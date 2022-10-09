package org.example.thread;

import java.util.HashMap;
import java.util.Map;

public class ConsoleThreadController implements ThreadController {

    private static final long TASK_WAITING_TIME = 20_000;
    private final Map<Integer, Thread> threads = new HashMap<>();
    private int threadIdCounter = 0;

    @Override
    public int start(Runnable task) {
        Thread threadTask = new Thread(task);
        int currentThreadId = ++threadIdCounter;
        threadTask.start();
        System.out.println("Task started [" + currentThreadId + "]");
        threads.put(currentThreadId, threadTask);
        return currentThreadId;
    }

    @Override
    public void stop(int localId) {
        Thread thread = threads.get(localId);
        if (thread == null || !thread.isAlive()) {
            System.out.println("Task not exists [" + localId + "]");
            return;
        }

        thread.interrupt();
        System.out.println("Task interrupted [" + localId + "]");
    }

    @Override
    public void await(int localId) {
        Thread thread = threads.get(localId);
        if (thread == null || !thread.isAlive()) {
            System.out.println("Task not exists [" + localId + "]");
            return;
        }

        System.out.println("Waiting for task [" + localId + "]");
        try {
            thread.join(TASK_WAITING_TIME);
            if (thread.isAlive()) {
                System.out.println("Task not responding [" + localId + "]");
            }
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public void stopAll() {
        threads.forEach((key, thread) -> {
            if (thread.isAlive()) {
                thread.interrupt();
                System.out.println("Task interrupted  [" + key + "]");
            }
        });
    }
}
