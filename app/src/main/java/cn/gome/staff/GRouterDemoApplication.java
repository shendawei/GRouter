package cn.gome.staff;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.gome.mobile.frame.router.GRouter;
import com.gome.mobile.frame.router.utils.LibraryUtils;

public class GRouterDemoApplication extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();
//        try {
        GRouter.getInstance().init();
        GRouter.getInstance().dispatcher(this, true);
        Log.d("GRouter", LibraryUtils.exist("cn.gome.staff.activity.Main2ActivityText").toString());
        Log.d("GRouter", LibraryUtils.exist("cn.gome.staff.activity.Main2Activity").toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }
}
