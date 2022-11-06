package dev.task.concurrent.semaphore;

public interface SimpleSemaphore {
    void acquire() throws InterruptedException;
    boolean tryAcquire();
    void release();
}
