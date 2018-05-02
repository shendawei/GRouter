package com.gome.mobile.frame.router;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.Set;

public class SchemeFilterActivity extends Activity {

    private static final String TAG = SchemeFilterActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        直接通过ARouter处理外部Uri
        Uri uri = getIntent().getData();
        Set<String> argsName = uri.getQueryParameterNames();
        Bundle bundle = null;
        if (!TextUtils.isEmpty(uri.getQuery())) {
            bundle = new Bundle();
            for (String key : argsName) {
                String value = uri.getQueryParameter(key);
                bundle.putString(key, value);
            }
        }
        GRouter.getInstance().build(uri).with(bundle).navigation(this);
        finish();
    }
}
