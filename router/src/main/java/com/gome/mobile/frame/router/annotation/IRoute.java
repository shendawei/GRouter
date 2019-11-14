package com.gome.mobile.frame.router.annotation;

import com.gome.mobile.frame.router.RequestMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface IRoute {
    String uri();

    RequestMethod method() default RequestMethod.Get;
}
