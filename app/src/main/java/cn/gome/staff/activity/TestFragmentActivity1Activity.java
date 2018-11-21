package cn.gome.staff.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import cn.gome.staff.R;
import cn.gome.staff.fragment.Test1Fragment;

public class TestFragmentActivity1Activity extends AppCompatActivity {

    private static final String TAG = "TestActivity1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment_activity1);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Test1Fragment()).commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Test1Fragment requestCode = " + requestCode + "; resultCode = " + resultCode);
    }
}
