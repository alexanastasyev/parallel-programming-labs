package org.example.thread;

public interface ThreadController {
    int start(Runnable runnable);
    void stop(int localId);
    void await(int localId);
    void stopAll();
}
