package cn.gome.staff.activity;

import android.app.Activity;
import android.os.Bundle;

import com.gome.mobile.frame.router.annotation.IRouter;

import cn.gome.staff.R;

//@IActivity(value = "abc", html = "/demo/inde.html", schemes = {"12313", "3213", "sdff"})
//@IActivity(value = "abc", html = "/demo/inde.html")
@IRouter("abc")
public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
