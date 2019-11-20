package com.gutils.appinit;

import android.app.Application;
import android.util.Log;

import com.gome.mobile.frame.router.App;
import com.gome.mobile.frame.router.annotation.IApp;

@IApp(priority = 1)
public class AppProxy implements App {
    @Override
    public void dispatcher(Application application) {
        Log.d("appinit","IApp Proxy");
    }
}
