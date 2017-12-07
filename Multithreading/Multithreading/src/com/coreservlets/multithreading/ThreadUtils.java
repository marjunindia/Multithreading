package com.coreservlets.multithreading;

public class ThreadUtils {
    public static void pause(double seconds) {
        try {
            Thread.sleep(Math.round(1000.0 * seconds));
        } catch (InterruptedException ie) {}
    }
    
    private ThreadUtils() {} // Uninstantiable class: static methods only
}
