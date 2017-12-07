package com.coreservlets.multithreading;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadSafeActivity extends Activity implements Runnable {
    private final static int LOOP_LIMIT = 3;
    private final static int POOL_SIZE = 4;
    private int mLatestThreadNum = 0;
    
    /** Initializes the app when it is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.race_conditions);
    }

    public void tryRace(View clickedButton) {
        ExecutorService taskList = 
                    Executors.newFixedThreadPool(POOL_SIZE);
        for(int i=0; i<POOL_SIZE; i++) {
            taskList.execute(this);
        }
    }
    
    // The only shared data is mLatestThreadNum, and we start the synchronization
    // before we read it and end the synchronization after we finish modifying it.
    // Also note that it is more efficient to move I/O outside of synchronized blocks,
    // but the print statement is left inside for parallelism with previous example.
    
    public void run() {
        int currentThreadNum;
        synchronized(this) {
            currentThreadNum = mLatestThreadNum;
            System.out.printf("Setting currentNum to %s%n", 
                              currentThreadNum);
            mLatestThreadNum = mLatestThreadNum + 1;
        }
        for (int i = 0; i < LOOP_LIMIT; i++) {
            System.out.printf("I am Counter %s; i is %s%n", 
                              currentThreadNum, i);
            ThreadUtils.pause(0.5 * Math.random());
        }
    }
}