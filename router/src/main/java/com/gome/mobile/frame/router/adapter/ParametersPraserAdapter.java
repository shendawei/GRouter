package com.gome.mobile.frame.router.adapter;

import android.os.Bundle;

/**
 * 参数解析适配器
 *
 * @author luciuszhang
 * @date 2018/05/11
 */
public interface ParametersPraserAdapter {


    /**
     * 解析参数方法
     *
     * @param url
     * @return
     */
    Bundle praser(String url);

    /**
     * 返回解析完成新的Url
     *
     * @param url
     * @return
     */
    String getUrl(String url);

}
