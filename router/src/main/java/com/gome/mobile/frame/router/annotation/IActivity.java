package com.gome.mobile.frame.router.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface IActivity {

    /**
     * 应用内部跳转链接地址
     * @return
     */
    String value();

    /**
     * 外部链接跳转应用地址
     * @return
     */
    String html();
}
