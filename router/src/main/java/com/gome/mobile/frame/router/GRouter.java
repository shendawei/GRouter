package com.gome.mobile.frame.router;

import java.lang.annotation.Annotation;
import java.util.HashMap;

/**
 * Gome IRouter
 *
 * @author hyxf
 * @date 2018/4/2 下午2:07
 */
public class GRouter {

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

    /**
     * 获取 class
     *
     * @param path
     * @return
     */
    public Class getClass(String path) {
        Class activityClass = mClassMap.get(path);
        if (activityClass == null) {
            throw new RuntimeException(String.format("Activity for path:%s not found", path));
        }
        return activityClass;
    }

}
