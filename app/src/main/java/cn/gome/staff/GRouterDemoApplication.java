package cn.gome.staff;

import android.app.Application;

import com.gome.mobile.frame.router.GRouter;

public class GRouterDemoApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            GRouter.getInstance().init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
