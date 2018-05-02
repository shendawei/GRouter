package cn.gome.staff.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gome.mobile.frame.router.IRouter;

import cn.gome.staff.R;

@IRouter("/demo/test4")
public class TestFromWebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_from_web);


        Bundle bundle = getIntent().getExtras();
        TextView tv = (TextView) findViewById(R.id.test_from_web_tv);
        if (bundle != null) {
            tv.setText("Arg1 = " + bundle.getString("arg1") + "; Arg2 = " + bundle.getString("arg2"));
        }
    }
}
