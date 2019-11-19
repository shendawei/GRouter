package com.gome.mobile.frame.router;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;

import cn.gome.mobile.frame.GThreadPool;

public class EventHandler {
    private final Object receiver;
    private final Method method;
    private final ThreadMode threadMode;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public EventHandler(Object receiver, Method method, ThreadMode threadMode) {
        this.receiver = receiver;
        this.method = method;
        this.threadMode = threadMode;
    }

    public void handle(final Bundle params) {
        Runnable invokeMethod = new Runnable() {
            @Override
            public void run() {
                invokeMethod(receiver, params);
            }
        };
        switch (threadMode) {
            case Main:
                mainHandler.post(invokeMethod);
                break;
            case Background:
                GThreadPool.runOnCalcThread(invokeMethod);
                break;
            case Posting:
            default:
                invokeMethod.run();
        }
    }

    private Object invokeMethod(Object receiver, Bundle params) {
        try {
            method.setAccessible(true);
            if (method.getParameterTypes().length == 0) {
                return method.invoke(receiver);
            } else {
                return method.invoke(receiver, params);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in invoking method: " + method.getName(), e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventHandler that = (EventHandler) o;

        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        return threadMode == that.threadMode;
    }

    @Override
    public int hashCode() {
        int h = method != null ? method.hashCode() : 0;
        h = 31 * h + (threadMode != null ? threadMode.hashCode() : 0);

        return h;
    }

    public Object getReceiver() {
        return receiver;
    }

    public Method getMethod() {
        return method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }
}
