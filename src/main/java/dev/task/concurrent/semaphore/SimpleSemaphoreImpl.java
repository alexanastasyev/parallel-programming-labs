package dev.task.concurrent.semaphore;

public class SimpleSemaphoreImpl implements SimpleSemaphore {

    private final Object lock = new Object();

    private final int permits;
    private int currentPermits;

    public SimpleSemaphoreImpl(int permits) {
        this.permits = permits;
        this.currentPermits = 0;
    }

    @Override
    public void acquire() throws InterruptedException {
        synchronized (lock) {
            while (currentPermits >= permits) {
                lock.wait();
            }
            currentPermits++;
        }
    }

    @Override
    public boolean tryAcquire() {
        synchronized (lock) {
            if (currentPermits < permits) {
                currentPermits++;
                return true;
            }
        }
        return false;
    }

    @Override
    public void release() {
        synchronized (lock) {
            currentPermits--;
            lock.notifyAll();
        }
    }

}
