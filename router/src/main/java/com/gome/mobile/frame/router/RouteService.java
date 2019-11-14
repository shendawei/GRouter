package com.gome.mobile.frame.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.gome.mobile.frame.router.intf.Result;

public interface RouteService {
    /**
     * Standard activity result: operation canceled.
     */
    int RESULT_CANCELED = Activity.RESULT_CANCELED;
    /**
     * Standard activity result: operation succeeded.
     */
    int RESULT_OK = Activity.RESULT_OK;
    /**
     * Start of user-defined activity results.
     */
    int RESULT_FIRST_USER = Activity.RESULT_FIRST_USER;

    void setActivityProxy(ActivityProxy activityProxy);

    Context getContext();

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode, Result result);

    void onActivityResult(int requestCode, int resultCode, Intent data, Result result);
}
