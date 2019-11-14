package cn.gome.staff.service;

import android.os.Bundle;

import com.gome.mobile.frame.router.annotation.IRoute;
import com.gome.mobile.frame.router.intf.Result;

public interface DemoService {

    @IRoute(uri = "/demo/getSync")
    String getString(Bundle params, Result result);

    @IRoute(uri = "/demo/getAsync")
    void getStringAsync(Bundle params, Result result);
}
