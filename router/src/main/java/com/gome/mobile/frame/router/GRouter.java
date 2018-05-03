package com.gome.mobile.frame.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.gome.mobile.frame.router.annotation.IActivity;
import com.gome.mobile.frame.router.annotation.IFragment;
import com.gome.mobile.frame.router.annotation.IRouter;
import com.gome.mobile.frame.router.annotation.IService;

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
    private static HashMap<String, Class> mServiceClassMap = new HashMap<>();
    private static HashMap<String, Class> mFragmentClassMap = new HashMap<>();
    private static HashMap<String, Class> mActivityClassMap = new HashMap<>();

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
        if (registerClass(className, IFragment.class)) {
            return;
        }
        if (registerClass(className, IService.class)) {
            return;
        }
        if (registerClass(className, IActivity.class)) {
            return;
        }
        registerClass(className, IRouter.class);
    }

    private static boolean registerClass(Class className, Class annotationType) throws Exception {
        Annotation anno = className.getAnnotation(annotationType);
        if (anno == null || annotationType == null) {
            return false;
        }
        String annotationTypeName = annotationType.getName();
        if (annotationTypeName.equals(IRouter.class.getName())) {
            IRouter router = (IRouter) anno;
            String key = router.value();
            if (mClassMap.containsKey(key)) {
                throw new RuntimeException("GRouter contains same path->" + key);
            }
            mClassMap.put(key, className);
            return true;
        }
        if (annotationTypeName.equals(IService.class.getName())) {
            IService service = (IService) anno;
            String key = service.value();
            if (mServiceClassMap.containsKey(key)) {
                throw new RuntimeException("GRouter contains same path->" + key);
            }
            mServiceClassMap.put(key, className);
            return true;
        }
        if (annotationTypeName.equals(IActivity.class.getName())) {
            IActivity activity = (IActivity) anno;
            String key = activity.value();
            if (mActivityClassMap.containsKey(key)) {
                throw new RuntimeException("GRouter contains same path->" + key);
            }
            mActivityClassMap.put(key, className);
            return true;
        }
        if (annotationTypeName.equals(IFragment.class.getName())) {
            IFragment fragment = (IFragment) anno;
            String key = fragment.value();
            if (mFragmentClassMap.containsKey(key)) {
                throw new RuntimeException("GRouter contains same path->" + key);
            }
            mFragmentClassMap.put(key, className);
            return true;
        }
        return false;
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

    private Class getServiceClass(String path) {
        Class targetClass = mServiceClassMap.get(path);
        if (targetClass == null) {
            throw new RuntimeException(String.format("service for path:%s not found", path));
        }
        return targetClass;
    }

    private Class getFragmentClass(String path) {
        Class targetClass = mFragmentClassMap.get(path);
        if (targetClass == null) {
            throw new RuntimeException(String.format("fragment for path:%s not found", path));
        }
        return targetClass;
    }

    private Class getActivityClass(String path) {
        Class targetClass = mActivityClassMap.get(path);
        if (targetClass == null) {
            throw new RuntimeException(String.format("activity for path:%s not found", path));
        }
        return targetClass;
    }


    /**
     * 获取 fragment实例
     * @param path
     * @param bundle
     * @return
     */
    public Fragment navigationFragment(String path, Bundle bundle) {
        Class fragmentClass = getFragmentClass(path);
        try {
            Object instance = fragmentClass.getConstructor().newInstance();
            if (instance instanceof Fragment && bundle != null) {
                ((Fragment)instance).setArguments(bundle);
            }
            return (Fragment) instance;
        } catch (Exception ex) {
            Log.e(TAG, "Fetch fragment instance error, " + ex.getStackTrace());
        }
        return null;
    }

    public Fragment navigationFragment(String path) {
        return navigationFragment(path, null);
    }


    public Object navigationService(String path) {
        Class serviceClass = getServiceClass(path);
        try {
            Object instance = serviceClass.getConstructor().newInstance();
            return instance;
        } catch (Exception ex) {
            Log.e(TAG, "Fetch Service instance error, " + ex.getStackTrace());
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
        if (uri == null || !(uri instanceof Uri) || TextUtils.isEmpty(uri.toString())) {
            throw new IllegalArgumentException("Parameter is invalid! uri = " + uri);
        } else {
            return new Postcard(uri.getPath());
        }
    }

    void navigation(Context context, Postcard postcard, int requestCode) {
        if (context == null) {
            throw new RuntimeException("Context is null");
        }
        if (null == postcard) {
            throw new RuntimeException(TAG + " :: No postcard!");
        }
        Class targetClass = getActivityClass(postcard.getPath());
        Intent intent = new Intent(context, targetClass);
        Bundle bundle = postcard.getBundle();
        if (bundle != null) {
            intent.putExtras(postcard.getBundle());
        }
        int flags = postcard.getFlags();
        if (flags != Integer.MAX_VALUE) {
            intent.addFlags(postcard.getFlags());
        }
        ((Activity)context).startActivityForResult(intent, requestCode);
    }

    public void navigation(Context context, String path, int requestCode) {
        navigation(context, new Postcard(path), requestCode);
    }

    public void navigation(Context context, String path) {
        navigation(context, new Postcard(path), -1);
    }

}
