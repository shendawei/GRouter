package cn.gome.staff.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gome.mobile.frame.router.annotation.IActivity;
import com.gome.mobile.frame.router.annotation.IRouter;

import cn.gome.staff.R;

@IActivity("/demo/test2")
public class Test2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String arg1 = bundle.getString("arg1");
            int arg2 = bundle.getInt("arg2");

            TextView tv = (TextView) findViewById(R.id.test_activity_param2);
            tv.setText("String args : " + arg1 + "; Integer args: " + arg2);
        }
    }
}
