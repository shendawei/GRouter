package com.gutils.appinit;

import android.app.Application;
import android.util.Log;

import com.gome.mobile.frame.router.App;
import com.gome.mobile.frame.router.annotation.IApp;

@IApp(priority = 2)
public class AppLoad implements App {
    @Override
    public void dispatcher(Application application, boolean debug) {
        Log.d("appinit","IApp Load");
    }
}
