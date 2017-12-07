package com.coreservlets.multithreading;

// The run method of this separate class could call 
// methods in the main class only if
// (A) You passed a reference to the main class to the constructor, and
// (B) The methods in the main class were public

public class SeparateCounter implements Runnable {
    private final int mLoopLimit;

    public SeparateCounter(int loopLimit) {
        this.mLoopLimit = loopLimit;
    }

    public void run() {
        for (int i = 0; i < mLoopLimit; i++) {
            String threadName = Thread.currentThread().getName();
            System.out.printf("%s: %s%n", threadName, i);
            ThreadUtils.pause(Math.random()); // Sleep for up to 1 second
        }
    }
}
