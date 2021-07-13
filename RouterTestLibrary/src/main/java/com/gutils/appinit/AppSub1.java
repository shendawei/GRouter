package com.gutils.appinit;

import android.app.Application;
import android.util.Log;

import com.gome.mobile.frame.router.App;
import com.gome.mobile.frame.router.annotation.IApp;

@IApp(priority = 100, subThread = true)
public class AppSub1 implements App {
    @Override
    public void dispatcher(Application application, boolean debug) {
        Log.d("GRouter", "AppSub1 Proxy");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
