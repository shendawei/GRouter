package com.gome.mobile.frame.router;

import android.app.Application;

/**
 * hyxf
 *
 * @author hyxf
 * @date 2019-11-20 15:58
 */
public interface App {
    /**
     * dispatcher
     *
     * @param application
     */
    void dispatcher(Application application, boolean debug);
}
