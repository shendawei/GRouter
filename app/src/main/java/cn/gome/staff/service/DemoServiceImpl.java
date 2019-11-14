package cn.gome.staff.service;

import android.content.Intent;
import android.os.Bundle;

import com.gome.mobile.frame.router.BaseRouteService;
import com.gome.mobile.frame.router.annotation.IService;
import com.gome.mobile.frame.router.intf.Result;

import cn.gome.staff.activity.ResultActivity;

@IService("/demo/service1")
public class DemoServiceImpl extends BaseRouteService implements DemoService {

    @Override
    public String getString(Bundle params, Result result) {
        return "A string from /demo/service1";
    }

    @Override
    public void getStringAsync(Bundle params, Result result) {
        startActivityForResult(new Intent(getContext(), ResultActivity.class), 100, result);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, Result result) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            result.success(data.getStringExtra("from"));
        }
    }
}
