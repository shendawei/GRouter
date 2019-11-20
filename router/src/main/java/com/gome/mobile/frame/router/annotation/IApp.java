package com.gome.mobile.frame.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * IApp
 *
 * @author hyxf
 * @date 2019-11-20 16:06
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface IApp {
    int priority() default 0;
}
