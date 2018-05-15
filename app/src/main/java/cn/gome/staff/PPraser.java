package cn.gome.staff;

import android.os.Bundle;

import com.gome.mobile.frame.router.adapter.ParametersPraserAdapter;

public abstract class PPraser implements ParametersPraserAdapter {

    @Override
    public String getUrl(String url) {
        return "p-";
    }
}
