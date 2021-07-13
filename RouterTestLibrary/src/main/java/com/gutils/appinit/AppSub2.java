package com.gutils.appinit;

import android.app.Application;
import android.util.Log;

import com.gome.mobile.frame.router.App;
import com.gome.mobile.frame.router.annotation.IApp;

@IApp(priority = 10, subThread = true)
public class AppSub2 implements App {
    @Override
    public void dispatcher(Application application, boolean debug) {
        Log.d("GRouter", "AppSub2 Proxy");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
