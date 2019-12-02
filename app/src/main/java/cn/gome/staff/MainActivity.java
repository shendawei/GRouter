package cn.gome.staff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.gome.mobile.frame.router.GRouter;
import com.gome.mobile.frame.router.Postcard;
import com.gome.mobile.frame.router.RequestMethod;
import com.gome.mobile.frame.router.ThreadMode;
import com.gome.mobile.frame.router.adapter.ParametersPraserAdapter;
import com.gome.mobile.frame.router.annotation.IRouteEvent;
import com.gome.mobile.frame.router.intf.NavigationCallback;
import com.gome.mobile.frame.router.intf.RequestCallback;

import cn.gome.staff.activity.TestFragmentActivity1Activity;
import cn.gome.staff.service.DemoService;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();

    @IRouteEvent(uri = "/demo/event", threadMode = ThreadMode.Main)
    public void onEvent(Bundle params) {
        Log.d("RouteDemoMain", "Got text: " + params.getString("text"));
        Toast.makeText(MainActivity.this, params.getString("text"), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestDemo1 demo1 = new TestDemo1();

        findViewById(R.id.router_routelayout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "OnClick toast here", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.router_broadcast).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GRouter.getInstance()
                        .build("/demo/event")
                        .withString("text", "A string from event")
                        .broadcast();
            }
        });

        findViewById(R.id.router_get_service).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DemoService service = (DemoService) GRouter.getInstance()
                        .navigationService(MainActivity.this, DemoService.class);
                String text = service.getString(null, null);

                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

        // 同步请求
        findViewById(R.id.router_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = (String) GRouter.getInstance()
                        .build("/demo/getSync")
                        .withMethod(RequestMethod.Get)
                        .request(MainActivity.this, null);
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

        // 异步请求
        findViewById(R.id.router_request_async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRouter.getInstance()
                        .build("/demo/getAsync")
                        .withMethod(RequestMethod.Get)
                        .request(MainActivity.this, new RequestCallback() {
                            @Override
                            public void onSuccess(Object value) {
                                Toast.makeText(MainActivity.this, value.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String errorCode, String errorMessage, Throwable cause) {

                            }
                        });
            }
        });

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
//                Fragment fragment = (Fragment) GRouter.getInstance().navigationFragment("/demo/test3_fragment");
                Fragment fragment = (Fragment) GRouter.getInstance().navigationFragment("/demo/test3_fragment", null, new NavigationCallback() {

                    @Override
                    public void onFound(Postcard postcard) {

                    }

                    @Override
                    public void onLost(Postcard postcard) {

                    }

                    @Override
                    public void onArrival(Postcard postcard) {

                    }

                    @Override
                    public void onInterrupt(Postcard postcard) {

                    }
                });
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

                GRouter.getInstance()
                        .build("/demo/webview")
                        .withString("url", "file:///android_asset/schame-test.html")
                        .withCallback(new NavigationCallback() {
                            @Override
                            public void onFound(Postcard postcard) {

                            }

                            @Override
                            public void onLost(Postcard postcard) {

                            }

                            @Override
                            public void onArrival(Postcard postcard) {

                            }

                            @Override
                            public void onInterrupt(Postcard postcard) {

                            }
                        })
                        .navigation(MainActivity.this);
            }
        });

        findViewById(R.id.router_interface).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRouter.getInstance().navigation(MainActivity.this, "/test/ktx");

                GRouter.getInstance().navigationService("demo/service", new NavigationCallback() {
                    @Override
                    public void onFound(Postcard postcard) {

                    }

                    @Override
                    public void onLost(Postcard postcard) {

                    }

                    @Override
                    public void onArrival(Postcard postcard) {

                    }

                    @Override
                    public void onInterrupt(Postcard postcard) {

                    }
                });
            }
        });


        /**
         * GRouter中
         * 从fragment使用startActivityForResult跳转到Activity,在使用setResult后要使fragment的onActivityResult方法收到回调，必须有：
         * 重写fragment的宿主activity的onActivityResult方法，并在方法内调用super.onActivityResult
         */
        findViewById(R.id.router_fragmentactivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestFragmentActivity1Activity.class);
                startActivity(intent);
            }
        });


        GRouter.getInstance().build("https://item.m.gomeplus.com/10/53/p|1|2|1130542649.html?stid=A00F")
                .withParameterPraser(new ParametersPraserAdapter() {
                    @Override
                    public Bundle praser(String url) {
                        int start = url.lastIndexOf("/");
                        String para = url.substring(start, url.length());
                        Bundle bundle = new Bundle();
                        bundle.putString("", "1130542649");
                        bundle.putString("stid", "A00F");
                        bundle.putString("url", "https://item.m.gomeplus.com/p.html?stid=A00F&sku=1130542649");


                        return bundle;
                    }

                    @Override
                    public String getUrl(String url) {

                        return "p-";
                    }
                }).navigation(this);


        GRouter.getInstance().build("https://item.m.gomeplus.com/p-1130542649.html?stid=A00F")
                .withParameterPraser(new PPraser() {
                    @Override
                    public Bundle praser(String url) {

                        return null;
                    }

                }).navigation(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        GRouter.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GRouter.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GRouter.getInstance().unregister(this);
    }
}
