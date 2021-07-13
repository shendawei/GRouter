package com.gome.mobile.frame.router.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * IApp
 *
 * @author hyxf
 * @date 2019-11-20 16:06
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface IApp {
    int priority() default 0;

    boolean subThread() default false;
}
