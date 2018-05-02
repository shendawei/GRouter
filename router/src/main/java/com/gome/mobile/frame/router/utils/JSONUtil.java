package com.gome.mobile.frame.router.utils;

import com.alibaba.fastjson.JSON;

public class JSONUtil {


    public static String object2Json(Object instance) {
        return JSON.toJSONString(instance);
    }

    public static <T> T json2Object(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

}
