package com.gome.mobile.frame.router;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ThreadPool {
    private static transient ThreadPool instance = null;

    public static ThreadPool getInstance() {
        if (instance == null) {
            synchronized (ThreadPool.class) {
                if (instance == null) {
                    instance = new ThreadPool();
                }
            }
        }

        return instance;
    }

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private ExecutorService backgroundExecutor = Executors.newFixedThreadPool(2);
    private ExecutorService asyncExecutor = Executors.newCachedThreadPool();

    private ThreadPool() {
    }

    void runOnMainThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

    void runOnBackground(Runnable runnable) {
        backgroundExecutor.submit(runnable);
    }

    void runOnIndividualThread(Runnable runnable) {
        asyncExecutor.submit(runnable);
    }
}
