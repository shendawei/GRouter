package com.gome.mobile.frame.router;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.gome.mobile.frame.router.annotation.IActivity;
import com.gome.mobile.frame.router.annotation.IApp;
import com.gome.mobile.frame.router.annotation.IFragment;
import com.gome.mobile.frame.router.annotation.IRoute;
import com.gome.mobile.frame.router.annotation.IRouter;
import com.gome.mobile.frame.router.annotation.IService;
import com.gome.mobile.frame.router.intf.NavigationCallback;
import com.gome.mobile.frame.router.intf.RequestCallback;
import com.gome.mobile.frame.router.intf.Result;
import com.gome.mobile.frame.router.utils.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.gome.mobile.frame.GThreadPool;

/**
 * Gome IRouter
 *
 * @author hyxf
 * @date 2018/4/2 下午2:07
 */
public class GRouter {

    private static final String TAG = GRouter.class.getName();
    private static final GRouter router = new GRouter();
    private static final ServiceManager mServiceManager = new ServiceManager();
    private static final EventManager mEventManager = new EventManager();

    private @Nullable
    WeakReference<Context> mContext = null;

    private GRouter() {
        GThreadPool.init();
    }

    /**
     * 获取IRouter单例
     *
     * @return
     */
    public synchronized static GRouter getInstance() {
        return router;
    }

    private static HashMap<String, Class> mClassMap = new HashMap<>();
    private static HashMap<String, Class> mServiceClassMap = new HashMap<>();
    private static HashMap<String, Class> mFragmentClassMap = new HashMap<>();
    private static HashMap<String, Class> mActivityClassMap = new HashMap<>();
    private static List<AppEntity> mAppClassList = new ArrayList<>();

    /**
     * 初始化GRouter
     */
    public void init() {
    }

    /**
     * dispatcher application
     *
     * @param application
     */
    public void dispatcher(Application application, boolean debug) {
        Collections.sort(mAppClassList, new Comparator<AppEntity>() {
            @Override
            public int compare(AppEntity t0, AppEntity t1) {
                return t0.getPriority().compareTo(t1.getPriority());
            }
        });
        for (AppEntity entity : mAppClassList) {
            Class appClass = entity.classApp;
            try {
                App app = (App) appClass.newInstance();
                app.dispatcher(application, debug);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setContext(Context context) {
        if (context != null) {
            mContext = new WeakReference<>(context);
        }
    }

    private Context getContext() {
        return mContext != null ? mContext.get() : null;
    }

    /**
     * 注册class
     *
     * @param className
     */
    private static void register(Class className) {
        if (registerIActivity(className)) {
            return;
        }
        if (registerIFragment(className)) {
            return;
        }
        if (registerIService(className)) {
            return;
        }
        if (registerIApp(className)) {
            return;
        }
        registerIRouter(className);
    }

    private static boolean registerIApp(Class className) {
        Annotation anno = className.getAnnotation(IApp.class);
        if (checkAnnotationNotNull(anno) && App.class.isAssignableFrom(className)) {
            IApp app = (IApp) anno;
            mAppClassList.add(new AppEntity(app.priority(), className));
            return true;
        }
        return false;
    }

    private static boolean registerIRouter(Class className) {
        Annotation anno = className.getAnnotation(IRouter.class);
        if (checkAnnotationNotNull(anno)) {
            IRouter router = (IRouter) anno;
            String key = router.value();
            if (mClassMap.containsKey(key)) {
                return true;
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
                return true;
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
                return true;
            }
            mServiceClassMap.put(key, className);
            mServiceManager.onRegister(key, className);

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
                return true;
            } else {
                mActivityClassMap.put(innerKey, className);
            }
            if (mActivityClassMap.containsKey(htmlKey)) {
                return true;
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
        return targetClass;
    }

    /**
     * 获取 fragment实例
     *
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
                ((Fragment) instance).setArguments(bundle);
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

        setContext(activity);

        Class targetClass = getActivityClass(postcard.getPath());
        NavigationCallback callback = postcard.getCallback();
        if (targetClass == null) {
            if (callback != null) {
                callback.onLost(postcard);
            }
            Log.e(TAG, "target Activity: " + postcard.getPath() + "  not found");
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

        setContext(fragment.getContext());

        Class targetClass = getActivityClass(postcard.getPath());
        NavigationCallback callback = postcard.getCallback();
        if (targetClass == null) {
            if (callback != null) {
                callback.onLost(postcard);
            }
            Log.e(TAG, "target Activity: " + postcard.getPath() + "  not found");
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

        setContext(context);

        Class targetClass = getActivityClass(postcard.getPath());
        NavigationCallback callback = postcard.getCallback();
        if (targetClass == null) {
            if (callback != null) {
                callback.onLost(postcard);
            }
            Log.e(TAG, "target Activity: " + postcard.getPath() + "  not found");
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

    private static Uri buildUri(String path, RequestMethod method) {
        return new Uri.Builder()
                .path(path)
                .appendQueryParameter("method", method.toString())
                .build();
    }

    Object navigationRequest(Context context, RequestCallback callback, Postcard postcard) {
        setContext(context);

        Uri uri = buildUri(postcard.getPath(), postcard.getMethod());

        return mServiceManager.onRequest(uri, postcard.getBundle(), callback);
    }

    /**
     * clazz是否存在
     *
     * @param clzName
     * @return
     */
    public boolean exists(Class clzName) {
        return mServiceClassMap.containsValue(clzName)
                || mActivityClassMap.containsValue(clzName)
                || mServiceClassMap.containsValue(clzName)
                || mFragmentClassMap.containsValue(clzName);
    }

    /**
     * 注册路径是否存在
     *
     * @param clzName
     * @return
     */
    public boolean exists(String clzName) {
        return mServiceClassMap.containsKey(clzName)
                || mActivityClassMap.containsKey(clzName)
                || mServiceClassMap.containsKey(clzName)
                || mFragmentClassMap.containsKey(clzName);
    }

    /**
     * 创建跳转参数对象
     *
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
     *
     * @param path 跳转用的uri
     * @return
     */
    public Postcard build(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Parameter is invalid! path empty");
        } else {
            return new Postcard(path);
        }
    }

    /**
     * 创建Fragment实例
     *
     * @param path   已注册的Fragment地址
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
     *
     * @param path 服务实例地址
     * @return
     */
    public Object navigationService(String path) {
        return navigationService(path, null);
    }

    /**
     * 从Activity跳转到Activity方法
     *
     * @param activity    调用跳转方法的Activity
     * @param path        跳转目标的地址
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
     *
     * @param fragment    调用跳转方法的fragment
     * @param path        跳转目标的地址
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
     *
     * @param application 调用跳转方法的application
     * @param path        跳转目标的地址
     */
    public void navigation(Application application, String path) {
        navigation(application, new Postcard(path));
    }

    /**
     * 从View跳转到Activity方法
     *
     * @param view 调用跳转方法的view
     * @param path 跳转目标的地址
     */
    public void navigation(View view, String path) {
        navigation(view.getContext(), new Postcard(path));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mServiceManager.onActivityResult(requestCode, resultCode, data);
    }

    public void register(Object receiver) {
        mEventManager.register(receiver);
    }

    public void unregister(Object receiver) {
        mEventManager.unregister(receiver);
    }

    public void broadcast(String uri, Bundle params) {
        mEventManager.onEvent(uri, params);
    }

    private static class ServiceHolder {
        final int id;
        final RouteService service;
        final Result result;

        ServiceHolder(int id, RouteService service, Result result) {
            this.id = id;
            this.service = service;
            this.result = result;
        }
    }

    private static class ServiceManager implements ActivityProxy {
        private static final int MAX_REQUEST_CODE = 0x00FF;
        private static final int MAX_SERIAL_NUMBER = 0x00FF;
        private static final int SERVICE_ID_MASK = 0xFF00;
        private static final int SERVICE_ID_SHIFT = 8;

        private int lastSerialNumber = 0;
        private List<ServiceHolder> pendingServices = new LinkedList<>();
        private Map<Uri, RequestHandler> handlers = new HashMap<>();

        private int generateServiceId() {
            return (lastSerialNumber = lastSerialNumber % MAX_SERIAL_NUMBER + 1) << SERVICE_ID_SHIFT;
        }

        void onRegister(String serviceUri, Class<?> serviceType) {
            Set<Class<?>> interfaces = ReflectUtil.getInheritedInterfaces(serviceType);
            if (!interfaces.contains(RouteService.class)) {
                return;
            }

            for (Class<?> anInterface : interfaces) {
                Set<Method> methods = ReflectUtil.findDeclaredMethodsByAnnotation(anInterface, IRoute.class);
                for (Method method : methods) {
                    IRoute route = method.getAnnotation(IRoute.class);
                    Uri uri = buildUri(route.uri(), route.method());

                    handlers.put(uri, new RequestHandler(serviceUri, method));
                }
            }
        }

        Object onRequest(Uri path, Bundle params, RequestCallback callback) {
            RequestHandler handler = handlers.get(path);
            if (handler == null) {
                return null;
            }

            Object serviceObj = router.navigationService(handler.getServiceUri());
            if (!(serviceObj instanceof RouteService)) {
                return null;
            }

            RouteService service = (RouteService) serviceObj;
            service.setActivityProxy(this);

            int serviceId = generateServiceId();
            Result result = new ResultImpl(serviceId, callback);

            pendingServices.add(new ServiceHolder(generateServiceId(), service, result));

            return handler.handle(service, params, result);
        }

        private ServiceHolder getServiceId(Object service) {
            for (ServiceHolder holder : pendingServices) {
                if (holder.service == service) {
                    return holder;
                }
            }

            return null;
        }

        private ServiceHolder getService(int serviceId) {
            for (ServiceHolder holder : pendingServices) {
                if (holder.id == serviceId) {
                    return holder;
                }
            }

            return null;
        }

        private ServiceHolder removeService(int serviceId) {
            ServiceHolder holder = getService(serviceId);
            if (holder != null) {
                pendingServices.remove(holder);
            }

            return holder;
        }

        @Override
        public Context getContext() {
            return router.getContext();
        }

        @Override
        public void startActivity(RouteService service, Intent intent) {
            Context context = getContext();
            if (context != null) {
                context.startActivity(intent);
            }
        }

        @Override
        public void startActivityForResult(RouteService service, Intent intent, int requestCode, Result result) {
            Context context = getContext();
            if (!(context instanceof Activity)) {
                return;
            }

            if (requestCode < 1 || requestCode > MAX_REQUEST_CODE) {
                throw new RuntimeException("requestCode must between 1 and " + MAX_REQUEST_CODE);
            }

            ServiceHolder holder = getServiceId(service);
            if (holder != null) {
                ((Activity) context).startActivityForResult(intent, requestCode + holder.id);
            }
        }

        void onActivityResult(int requestCode, int resultCode, Intent data) {
            int serviceId = requestCode & SERVICE_ID_MASK;
            requestCode -= serviceId;

            ServiceHolder holder = getService(serviceId);
            if (holder != null) {
                holder.service.onActivityResult(requestCode, resultCode, data, holder.result);
            }
        }

        private class ResultImpl implements Result {
            private final int serviceId;
            private final RequestCallback callback;

            private ResultImpl(int serviceId, RequestCallback callback) {
                this.serviceId = serviceId;
                this.callback = callback;
            }

            @Override
            public void success(Object obj) {
                if (callback != null) {
                    callback.onSuccess(obj);
                }
                removeService(serviceId);
            }

            @Override
            public void error(String errorCode, String errorMessage, Throwable cause) {
                if (callback != null) {
                    callback.onError(errorCode, errorMessage, cause);
                }
                removeService(serviceId);
            }

            @Override
            public void error(String errorMessage, Throwable cause) {
                error(null, errorMessage, cause);
            }
        }
    }

    private static class AppEntity {
        private Integer priority;
        private Class classApp;

        public AppEntity(int priority, Class classApp) {
            this.priority = priority;
            this.classApp = classApp;
        }

        public Integer getPriority() {
            return priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public Class getClassApp() {
            return classApp;
        }

        public void setClassApp(Class classApp) {
            this.classApp = classApp;
        }
    }

}
