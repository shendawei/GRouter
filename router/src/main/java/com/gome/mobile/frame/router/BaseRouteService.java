package com.gome.mobile.frame.router;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gome.mobile.frame.router.intf.Result;

public class BaseRouteService implements RouteService {
    private @Nullable
    ActivityProxy activityProxy;

    @Override
    public void setActivityProxy(@NonNull ActivityProxy activityProxy) {
        this.activityProxy = activityProxy;
    }

    @Override
    public Context getContext() {
        if (activityProxy != null) {
            return activityProxy.getContext();
        }

        return null;
    }

    @Override
    public void startActivity(Intent intent) {
        if (activityProxy != null) {
            activityProxy.startActivity(this, intent);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Result result) {
        if (activityProxy != null) {
            activityProxy.startActivityForResult(this, intent, requestCode, result);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, Result result) {
    }
}
