package com.gome.mobile.frame.router.intf;

import com.gome.mobile.frame.router.Postcard;

/**
 * Callback in navigation.
 *
 * @author luciuszhang
 * @date 2018/05/09
 */
public interface NavigationCallback {

    /**
     * Callback when find the destination.
     *
     * @param postcard meta
     */
    void onFound(Postcard postcard);

    /**
     * Callback after lose your way.
     *
     * @param postcard meta
     */
    void onLost(Postcard postcard);

    /**
     * Callback after navigation.
     *
     * @param postcard meta
     */
    void onArrival(Postcard postcard);

    /**
     * Callback on interrupt.
     *
     * @param postcard meta
     */
    void onInterrupt(Postcard postcard);
}
