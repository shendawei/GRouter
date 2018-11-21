package cn.gome.staff.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gome.mobile.frame.router.annotation.IActivity;

import cn.gome.staff.R;
import cn.gome.staff.fragment.Test2Fragment;

@IActivity("/test2")
public class TestFragmentActivity2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment_activity2);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Test2Fragment()).commit();
    }
}
