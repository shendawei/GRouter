package cn.gome.staff.activity;

import android.app.Activity;
import android.os.Bundle;

import com.gome.mobile.frame.router.IRouter;

import cn.gome.staff.R;

@IRouter("abc")
public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
