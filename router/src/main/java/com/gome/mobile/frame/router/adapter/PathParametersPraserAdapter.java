package com.gome.mobile.frame.router.adapter;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.Set;

/**
 * url参数解析器
 *
 * @author luciuszhang
 * @date 2018/05/11
 */
public class PathParametersPraserAdapter implements ParametersPraserAdapter {

    @Override
    public Bundle praser(String paramsUrl) {
        Bundle bundle = null;
        if (TextUtils.isEmpty(paramsUrl)) {
            return bundle;
        }
        Uri uri = Uri.parse(paramsUrl);
        if (uri == null) {
            return bundle;
        }
        Set<String> argsName = uri.getQueryParameterNames();
        if (!TextUtils.isEmpty(uri.getQuery())) {
            bundle = new Bundle();
            for (String key : argsName) {
                String value = uri.getQueryParameter(key);
                bundle.putString(key, value);
            }
        }
        return bundle;
    }

    @Override
    public String getUrl(String paramsUrl) {
        if (TextUtils.isEmpty(paramsUrl)) {
            throw new IllegalStateException("paramsUrl is empty");
        }
        Uri uri = Uri.parse(paramsUrl);
        if (uri == null) {
            throw new IllegalArgumentException("paramsUrl isn't a scheme uri");
        }
        return uri.getPath();
    }
}
