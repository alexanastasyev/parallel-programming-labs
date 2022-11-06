import dev.task.concurrent.semaphore.SimpleSemaphore;
import dev.task.concurrent.semaphore.SimpleSemaphoreImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleSemaphoreTest {

    @RepeatedTest(100)
    public void testTwoThreadsAsync() {
        AtomicBoolean firstDone = new AtomicBoolean(false);
        AtomicBoolean secondDone = new AtomicBoolean(false);

        SimpleSemaphore semaphore = new SimpleSemaphoreImpl(1);
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            try {
                latch.await();
                semaphore.acquire();
                firstDone.set(true);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } finally {
                semaphore.release();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                latch.await();
                semaphore.acquire();
                secondDone.set(true);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } finally {
                semaphore.release();
            }
        });

        thread1.start();
        thread2.start();

        latch.countDown();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        Assertions.assertTrue(firstDone.get());
        Assertions.assertTrue(secondDone.get());
    }

    @RepeatedTest(100)
    public void testTwoThreadsSync() {
        AtomicBoolean firstDone = new AtomicBoolean(false);
        AtomicBoolean secondDone = new AtomicBoolean(false);

        SimpleSemaphore semaphore = new SimpleSemaphoreImpl(1);
        CountDownLatch latchThreadsStart = new CountDownLatch(1);
        CountDownLatch latchSecondThreadWait = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            try {
                latchThreadsStart.await();
                semaphore.acquire();
                firstDone.set(true);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } finally {
                semaphore.release();
                latchSecondThreadWait.countDown();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                latchThreadsStart.await();
                latchSecondThreadWait.await();
                semaphore.acquire();
                secondDone.set(true);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } finally {
                semaphore.release();
            }
        });

        thread1.start();
        thread2.start();

        latchThreadsStart.countDown();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        Assertions.assertTrue(firstDone.get());
        Assertions.assertTrue(secondDone.get());
    }

    @RepeatedTest(100)
    public void testManyThreads() {
        int permits = 5;
        SimpleSemaphore semaphore = new SimpleSemaphoreImpl(permits);
        AtomicBoolean wasExceeded = new AtomicBoolean(false);
        AtomicInteger currentRunning = new AtomicInteger(0);
        CountDownLatch latchStart = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(() -> {
                try {
                    latchStart.await();
                    semaphore.acquire();
                    if (currentRunning.incrementAndGet() > permits) {
                        wasExceeded.set(true);
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                } finally {
                    currentRunning.decrementAndGet();
                    semaphore.release();
                }
            }));
        }

        threads.forEach(Thread::start);
        latchStart.countDown();
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });

        Assertions.assertFalse(wasExceeded.get());
        Assertions.assertEquals(0, currentRunning.get());
    }

    @Test
    public void testTryAcquire() {
        SimpleSemaphore semaphore = new SimpleSemaphoreImpl(1);
        Assertions.assertTrue(semaphore.tryAcquire());
        Assertions.assertFalse(semaphore.tryAcquire());
        semaphore.release();
        Assertions.assertTrue(semaphore.tryAcquire());
    }

    @RepeatedTest(100)
    public void testAcquireException() {
        SimpleSemaphore semaphore = new SimpleSemaphoreImpl(1);
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            try {
                semaphore.acquire();
                latch.countDown();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                latch.await();
                Assertions.assertThrows(InterruptedException.class, semaphore::acquire);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });

        thread1.start();
        thread2.start();
        thread2.interrupt();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

}
