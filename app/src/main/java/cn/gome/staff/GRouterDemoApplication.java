package cn.gome.staff;

import android.support.multidex.MultiDexApplication;

import com.gome.mobile.frame.router.GRouter;

public class GRouterDemoApplication extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            GRouter.getInstance().init();
            GRouter.getInstance().dispatcher(this,BuildConfig.DEBUG);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }
}
