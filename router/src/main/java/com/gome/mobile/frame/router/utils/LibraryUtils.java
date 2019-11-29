package com.gome.mobile.frame.router.utils;

/**
 * 组件库工具
 *
 * @author hyxf
 * @date 2019-11-29 11:48
 */
public class LibraryUtils {

    /**
     * 判断三方库是否存在
     *
     * @param mainClass 三方库内关键 Class
     * @return
     */
    public static Boolean exist(String mainClass) {
        Boolean exist = false;
        try {
            exist = Class.forName(mainClass) != null;
        } catch (Exception e) {
        }
        return exist;
    }
}
