package cn.gome.staff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gome.mobile.frame.router.GRouter;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestDemo1 demo1 = new TestDemo1();

        // 标准跳转
        findViewById(R.id.router_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click here");
                startActivity(new Intent(MainActivity.this, GRouter.getInstance().getClass("abc")));
            }
        });

        // 标砖 @IRouter("/demo/test1")
        findViewById(R.id.router_grouter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRouter.getInstance().navigation(MainActivity.this, "/demo/test1");
            }
        });

        // 标砖跳转带参数
        findViewById(R.id.router_grouter_with_params).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRouter.getInstance()
                        .build("/demo/test2")
                        .withString("arg1", "String")
                        .withInt("arg2", 123456)
                        .navigation(MainActivity.this);
            }
        });

        // 获取Fragment实例
        findViewById(R.id.router_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = (Fragment) GRouter.getInstance().navigationFragment("/demo/test3_fragment");
                Toast.makeText(MainActivity.this, "fragment Instance class: " + fragment.getClass().getName(), Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.router_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRouter.getInstance()
                        .build("/demo/webview")
                        .withString("url", "file:///android_asset/schame-test.html")
                        .navigation(MainActivity.this);
            }
        });

        findViewById(R.id.router_interface).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
