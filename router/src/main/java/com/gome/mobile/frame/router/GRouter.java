package com.gome.mobile.frame.router;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.gome.mobile.frame.router.annotation.IActivity;
import com.gome.mobile.frame.router.annotation.IFragment;
import com.gome.mobile.frame.router.annotation.IRouter;
import com.gome.mobile.frame.router.annotation.IService;
import com.gome.mobile.frame.router.intf.NavigationCallback;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;

/**
 * Gome IRouter
 *
 * @author hyxf
 * @date 2018/4/2 下午2:07
 */
public class GRouter {

    private static final String TAG = GRouter.class.getName();
    private static final GRouter router = new GRouter();

    private GRouter() {}

    /**
     * 获取IRouter单例
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
    public void init() {
    }

    /**
     * 注册class
     *
     * @param className
     */
    private static void register(Class className){
        if (registerIActivity(className)) {
            return;
        }
        if (registerIFragment(className)) {
            return;
        }
        if (registerIService(className)) {
            return;
        }
        registerIRouter(className);
    }

    private static boolean registerIRouter(Class className) {
        Annotation anno = className.getAnnotation(IRouter.class);
        if (checkAnnotationNotNull(anno)) {
            IRouter router = (IRouter) anno;
            String key = router.value();
            if (mClassMap.containsKey(key)) {
                throw new RuntimeException("GRouter contains same path->" + key);
            }
            mClassMap.put(key, className);
            return true;
        }
        return false;
    }

    private static boolean registerIFragment(Class className) {
        Annotation anno = className.getAnnotation(IFragment.class);
        if (checkAnnotationNotNull(anno)) {
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

    private static boolean registerIService(Class className) {
        Annotation anno = className.getAnnotation(IService.class);
        if (checkAnnotationNotNull(anno)) {
            IService service = (IService) anno;
            String key = service.value();
            if (mServiceClassMap.containsKey(key)) {
                throw new RuntimeException("GRouter contains same path->" + key);
            }
            mServiceClassMap.put(key, className);
            return true;
        }
        return false;
    }

    private static boolean registerIActivity(Class className) {
        Annotation anno = className.getAnnotation(IActivity.class);
        if (checkAnnotationNotNull(anno)) {
            IActivity activity = (IActivity) anno;
            String innerKey = activity.value();
            String htmlKey = activity.html();
            if (innerKey != null && mActivityClassMap.containsKey(innerKey)) {
                throw new RuntimeException("GRouter contains same path->" + innerKey);
            } else {
                mActivityClassMap.put(innerKey, className);
            }
            if (mActivityClassMap.containsKey(htmlKey)) {
                throw new RuntimeException("GRouter contains same path->" + htmlKey);
            } else if (htmlKey != null && htmlKey.length() > 0) {
                mActivityClassMap.put(htmlKey, className);
            }
            return true;
        }
        return false;
    }

    private static boolean checkAnnotationNotNull(Annotation anno) {
        if (anno == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取 class
     *
     * @param path
     * @return
     */
    public Class getClass(String path) {
        Class targetClass = mClassMap.get(path);
        return targetClass;
    }

    private Class getServiceClass(String path) {
        Class targetClass = mServiceClassMap.get(path);
        return targetClass;
    }

    private Class getFragmentClass(String path) {
        Class targetClass = mFragmentClassMap.get(path);
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
    public Fragment navigationFragment(String path, Bundle bundle, NavigationCallback callback) {
        Class fragmentClass = getFragmentClass(path);
        if (fragmentClass == null) {
            if (callback != null) {
                callback.onLost(new Postcard(path).with(bundle));
            }
            Log.e(TAG, "target Fragment: " + path + " nor found!");
            return null;
        } else {
            if (callback != null) {
                callback.onFound(new Postcard(path).with(bundle));
            }
        }
        try {
            Object instance = fragmentClass.getConstructor().newInstance();
            if (instance instanceof Fragment && bundle != null) {
                ((Fragment)instance).setArguments(bundle);
            }
            return (Fragment) instance;
        } catch (Exception ex) {
            Log.e(TAG, "Fetch fragment instance error, " + ex.getStackTrace());
            if (callback != null) {
                callback.onInterrupt(new Postcard(path).with(bundle));
            }
        }
        if (callback != null) {
            callback.onArrival(new Postcard(path).with(bundle));
        }
        return null;
    }

    public Object navigationService(String path, NavigationCallback callback) {
        Class serviceClass = getServiceClass(path);
        if (serviceClass == null) {
            if (callback != null) {
                callback.onLost(new Postcard(path));
            }
        } else {
            if (callback != null) {
                callback.onFound(new Postcard(path));
            }
        }
        try {
            Object instance = serviceClass.getConstructor().newInstance();
            return instance;
        } catch (Exception ex) {
            Log.e(TAG, "Fetch Service instance error, " + ex.getStackTrace());
            if (callback != null) {
                callback.onInterrupt(new Postcard(path));
            }
        }
        if (callback != null) {
            callback.onArrival(new Postcard(path));
        }
        return null;
    }

    void navigation(Activity activity, Postcard postcard, int requestCode) {
        if (activity == null) {
            throw new RuntimeException("Context is null");
        }
        if (null == postcard) {
            throw new RuntimeException(TAG + " :: No postcard!");
        }
        Class targetClass = getActivityClass(postcard.getPath());
        NavigationCallback callback = postcard.getCallback();
        if (targetClass == null) {
            if (callback != null) {
                callback.onLost(postcard);
            }
            Log.e(TAG, "target Activity: "+ postcard.getPath() +"  not found");
            return;
        } else {
            if (callback != null) {
                callback.onFound(postcard);
            }
        }
        Intent intent = new Intent(activity, targetClass);
        Bundle bundle = postcard.getBundle();
        if (bundle != null) {
            intent.putExtras(postcard.getBundle());
        }
        int flags = postcard.getFlags();
        if (flags != Integer.MAX_VALUE) {
            intent.addFlags(postcard.getFlags());
        }
        activity.startActivityForResult(intent, requestCode);
        if (callback != null) {
            callback.onArrival(postcard);
        }
    }

    void navigation(Fragment fragment, Postcard postcard, int requestCode) {
        if (fragment == null) {
            throw new RuntimeException("Context is null");
        }
        if (null == postcard) {
            throw new RuntimeException(TAG + " :: No postcard!");
        }
        Class targetClass = getActivityClass(postcard.getPath());
        NavigationCallback callback = postcard.getCallback();
        if (targetClass == null) {
            if (callback != null) {
                callback.onLost(postcard);
            }
            Log.e(TAG, "target Activity: "+ postcard.getPath() +"  not found");
            return;
        } else {
            if (callback != null) {
                callback.onFound(postcard);
            }
        }
        Intent intent = new Intent(fragment.getActivity(), targetClass);
        Bundle bundle = postcard.getBundle();
        if (bundle != null) {
            intent.putExtras(postcard.getBundle());
        }
        int flags = postcard.getFlags();
        if (flags != Integer.MAX_VALUE) {
            intent.addFlags(postcard.getFlags());
        }
        fragment.startActivityForResult(intent, requestCode);
        if (callback != null) {
            callback.onArrival(postcard);
        }
    }

    void navigation(Context context, Postcard postcard) {
        if (context == null) {
            throw new RuntimeException("context is null");
        }
        if (null == postcard) {
            throw new RuntimeException(TAG + " :: No postcard!");
        }
        Class targetClass = getActivityClass(postcard.getPath());
        NavigationCallback callback = postcard.getCallback();
        if (targetClass == null) {
            if (callback != null) {
                callback.onLost(postcard);
            }
            Log.e(TAG, "target Activity: "+ postcard.getPath() +"  not found");
            return;
        } else {
            if (callback != null) {
                callback.onFound(postcard);
            }
        }
        Intent intent = new Intent(context, targetClass);
        Bundle bundle = postcard.getBundle();
        if (bundle != null) {
            intent.putExtras(postcard.getBundle());
        }
        int flags = postcard.getFlags();
        if (flags != Integer.MAX_VALUE) {
            intent.addFlags(postcard.getFlags());
        }
        context.startActivity(intent);
        if (callback != null) {
            callback.onArrival(postcard);
        }
    }

    /**
     * 创建跳转参数对象
     * @param uri 跳转用的uri
     * @return
     */
    public Postcard build(Uri uri) {
        if (uri == null || !(uri instanceof Uri) || TextUtils.isEmpty(uri.toString())) {
            throw new IllegalArgumentException("Parameter is invalid! uri = " + uri);
        } else {
            Set<String> argsName = uri.getQueryParameterNames();
            Bundle bundle = null;
            if (!TextUtils.isEmpty(uri.getQuery())) {
                bundle = new Bundle();
                for (String key : argsName) {
                    String value = uri.getQueryParameter(key);
                    bundle.putString(key, value);
                }
            }
            Postcard postcard = new Postcard(uri.getPath());
            if (bundle != null) {
                postcard.with(bundle);
            }
            return postcard;
        }
    }

    /**
     * 创建跳转参数对象
     * @param path 跳转用的uri
     * @return
     */
    public Postcard build(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Parameter is invalid! path = " + path);
        } else {
            return new Postcard(path);
        }
    }

    /**
     * 创建Fragment实例
     * @param path 已注册的Fragment地址
     * @param bundle 需要传给Fragment的参数
     * @return
     */
    public Fragment navigationFragment(String path, Bundle bundle) {
        return navigationFragment(path, bundle, null);
    }

    public Fragment navigationFragment(String path) {
        return navigationFragment(path, null);
    }

    /**
     * 获取服务的实例
     * @param path 服务实例地址
     * @return
     */
    public Object navigationService(String path) {
        return navigationService(path, null);
    }

    /**
     * 从Activity跳转到Activity方法
     * @param activity 调用跳转方法的Activity
     * @param path 跳转目标的地址
     * @param requestCode
     */
    public void navigation(Activity activity, String path, int requestCode) {
        navigation(activity, new Postcard(path), requestCode);
    }

    public void navigation(Activity activity, String path) {
        navigation(activity, new Postcard(path), -1);
    }

    /**
     * 从Fragment跳转到Activity方法
     * @param fragment 调用跳转方法的fragment
     * @param path 跳转目标的地址
     * @param requestCode
     */
    public void navigation(Fragment fragment, String path, int requestCode) {
        navigation(fragment, new Postcard(path), requestCode);
    }

    public void navigation(Fragment fragment, String path) {
        navigation(fragment, new Postcard(path), -1);
    }

    /**
     * 从Application跳转到Activity方法
     * @param application 调用跳转方法的application
     * @param path 跳转目标的地址
     */
    public void navigation(Application application, String path) {
        navigation(application, new Postcard(path));
    }

    /**
     * 从View跳转到Activity方法
     * @param view 调用跳转方法的view
     * @param path 跳转目标的地址
     */
    public void navigation(View view, String path) {
        navigation(view.getContext(), new Postcard(path));
    }

}
