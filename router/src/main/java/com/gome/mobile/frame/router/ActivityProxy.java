package com.gome.mobile.frame.router;

import android.content.Context;
import android.content.Intent;

import com.gome.mobile.frame.router.intf.Result;

interface ActivityProxy {
    Context getContext();

    void startActivity(RouteService service, Intent intent);

    void startActivityForResult(RouteService service, Intent intent, int requestCode, Result result);
}
