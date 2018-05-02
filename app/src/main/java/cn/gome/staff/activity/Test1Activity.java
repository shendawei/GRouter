package cn.gome.staff.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gome.mobile.frame.router.IRouter;

import cn.gome.staff.R;

@IRouter("/demo/test1")
public class Test1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
    }
}
