package com.gome.mobile.frame.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.util.HashMap;

/**
 * Gome IRouter
 *
 * @author hyxf
 * @date 2018/4/2 下午2:07
 */
public class GRouter {

    private static final String TAG = GRouter.class.getName();
    private static final GRouter router = new GRouter();

    private GRouter() {
    }

    /**
     * 获取单例
     *
     * @return
     */
    public synchronized static GRouter getInstance() {
        return router;
    }

    private static HashMap<String, Class> mClassMap = new HashMap<String, Class>();

    /**
     * 初始化GRouter
     */
    public void init() throws Exception {
    }

    /**
     * 注册class
     *
     * @param className
     */
    private static void register(Class className) throws Exception {
        Annotation anno = className.getAnnotation(IRouter.class);
        if (anno != null) {
            IRouter router = (IRouter) anno;
            String key = router.value();
            if (mClassMap.containsKey(key)) {
                throw new RuntimeException("GRouter contains same path->" + key);
            }
            mClassMap.put(key, className);
        }
    }

    void navigation(Context context, Postcard postcard, int requestCode) {
        if (context == null) {
            throw new RuntimeException("Context is null");
        }
        if (null == postcard) {
            throw new RuntimeException(TAG + " :: No postcard!");
        }
        Class targetClass = getClass(postcard.getPath());
        Intent intent = new Intent(context, targetClass);
        intent.putExtras(postcard.getBundle());
        intent.addFlags(postcard.getFlags());
        ((AppCompatActivity)context).startActivityForResult(intent, requestCode);

    }

    /**
     * 获取 class
     *
     * @param path
     * @return
     */
    public Class getClass(String path) {
        Class targetClass = mClassMap.get(path);
        if (targetClass == null) {
            throw new RuntimeException(String.format("Activity for path:%s not found", path));
        }
        return targetClass;
    }


    /**
     * 获取 fragment实例
     * @param path
     * @param bundle
     * @return
     */
    public Object getFragmentInstance(String path, Bundle bundle) {
        Class fragmentClass = getClass(path);
        try {
            Object instance = fragmentClass.getConstructor().newInstance();
            if (instance instanceof Fragment) {
                ((Fragment)instance).setArguments(bundle);
            }
            return instance;
        } catch (Exception ex) {
            Log.e(TAG, "Fetch fragment instance error, " + ex.getStackTrace());
        }
        return null;
    }


    public Postcard build(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Parameter is invalid! path = " + path);
        } else {
            return new Postcard(path);
        }
    }

    public Postcard build(Uri uri) {
        if (uri == null || TextUtils.isEmpty(uri.toString())) {
            throw new IllegalArgumentException("Parameter is invalid! uri = " + uri);
        } else {
            return new Postcard(uri.getPath());
        }
    }

    public void navigation(Context context, String path, int requestCode) {
        navigation(context, new Postcard(path), requestCode);
    }

    public void navigation(Context context, String path) {
        navigation(context, new Postcard(path), -1);
    }

}
