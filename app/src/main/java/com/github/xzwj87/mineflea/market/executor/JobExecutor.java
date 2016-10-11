package com.github.xzwj87.mineflea.market.executor;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by jason on 9/27/16.
 */

public class JobExecutor implements Executor {

    private static final int INI_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;

    private static final int MAX_KEEP_ALIVE_TIME = 10;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private ThreadPoolExecutor mPoolExecutor;
    private BlockingDeque<Runnable> mJobQueue;

    @Inject
    public JobExecutor(){
        mJobQueue = new LinkedBlockingDeque<>();
        mPoolExecutor = new ThreadPoolExecutor(INI_POOL_SIZE,MAX_POOL_SIZE,
                MAX_KEEP_ALIVE_TIME,KEEP_ALIVE_TIME_UNIT,mJobQueue);
    }

    @Override
    public void execute(Runnable cmd) {
        if(cmd == null){
            throw new IllegalArgumentException("runnable should not be null");
        }

        mPoolExecutor.execute(cmd);
    }
}
