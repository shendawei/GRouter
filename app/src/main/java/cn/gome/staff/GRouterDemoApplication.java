package cn.gome.staff;

import android.app.Application;
import android.content.Intent;

import com.gome.mobile.frame.router.GRouter;

public class GRouterDemoApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        try {
            GRouter.getInstance().init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
