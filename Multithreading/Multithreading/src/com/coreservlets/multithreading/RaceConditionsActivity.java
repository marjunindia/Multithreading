package com.coreservlets.multithreading;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RaceConditionsActivity extends Activity implements Runnable {
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
    
    // Because the current thread could get preempted after
    // it reads the latest thread number but before it updates it,
    // this code is unsafe!
  
    public void run() {
        int currentThreadNum = mLatestThreadNum;
        System.out.printf("Setting currentNum to %s%n", 
                          currentThreadNum);
        mLatestThreadNum = mLatestThreadNum + 1;
        for (int i = 0; i < LOOP_LIMIT; i++) {
            System.out.printf("I am Counter %s; i is %s%n", 
                              currentThreadNum, i);
            ThreadUtils.pause(0.5 * Math.random());
        }
    }
}