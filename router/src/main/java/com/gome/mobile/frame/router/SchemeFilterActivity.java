package com.gome.mobile.frame.router;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

public class SchemeFilterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        直接通过ARouter处理外部Uri
        Uri uri = getIntent().getData();
        GRouter.getInstance().build(uri.toString()).navigation(this);
    }
}
