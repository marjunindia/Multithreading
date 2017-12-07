package com.coreservlets.multithreading;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadingBasicsActivity extends Activity implements Runnable {
    private final static int LOOP_LIMIT = 5;
    
    /** Initializes the app when it is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threading_basics);
    }

    /** Starts a counter that uses option 1: 
     *  a separate class that implements Runnable.
     *  Invoked via a Button in the main window.
     */
    public void separateClass(View clickedButton) {
        ExecutorService taskList = 
                    Executors.newFixedThreadPool(50);
        taskList.execute(new SeparateCounter(6));
        taskList.execute(new SeparateCounter(5));
        taskList.execute(new SeparateCounter(4));
    }
    
    /** Starts a counter that uses option 2: 
     *  the main class (the Activity, in this case) implements Runnable.
     *  Invoked via a Button in the main window.
     */
    public void implementsInterface(View clickedButton) {
        ExecutorService taskList = 
                    Executors.newFixedThreadPool(50);
        taskList.execute(this);
        taskList.execute(this);
        taskList.execute(this);
    }

    /** Starts a counter that uses option 3: 
     *  an inner class that implements Runnable.
     *  Invoked via a Button in the main window.
     */
    public void innerClass(View clickedButton) {
        ExecutorService taskList = 
                    Executors.newFixedThreadPool(50);
        taskList.execute(new InnerCounter(6));
        taskList.execute(new InnerCounter(5));
        taskList.execute(new InnerCounter(4));
    }
    
    // This run method can easily access methods and instance variables in the
    // main class, since it is inside the main class. If you start multiple threads
    // and run modifies instance variables, you have to worry about race conditions.
    // It is very difficult to pass arguments to run, so each thread does almost
    // the same thing. If you want to pass arguments, use Runnable inner classes instead.
    @Override
    public void run() {
        for (int i = 0; i < LOOP_LIMIT; i++) {
            String threadName = Thread.currentThread().getName();
            System.out.printf("%s: %s%n", threadName, i);
            ThreadUtils.pause(Math.random()); // Sleep for up to 1 second
        }
    }
    
    private class InnerCounter implements Runnable {
        private final int mLoopLimit;

        public InnerCounter(int loopLimit) {
            this.mLoopLimit = loopLimit;
        }

        // The run method can easily access methods and instance variables in the
        // main class, since it is in an inner class inside the main class. If you
        // start multiple threads and run modifies instance variables of the outer
        // class, you have to worry about race conditions. It is very simple to
        // pass arguments to run by passing them to the inner class constructor
        // and then just having run refer to them.
        
        public void run() {
            for (int i = 0; i < mLoopLimit; i++) {
                String threadName = Thread.currentThread().getName();
                System.out.printf("%s: %s%n", threadName, i);
                ThreadUtils.pause(Math.random()); // Sleep for up to 1 second
            }
        } 
    }
}