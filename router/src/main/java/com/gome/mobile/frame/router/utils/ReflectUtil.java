package com.gome.mobile.frame.router.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ReflectUtil {

    @NonNull
    public static List<Class<?>> getInterfaceGenericClasses(Class<?> clazz, Class<?> interfaceType) {
        List<Class<?>> result = new LinkedList<>();
        if (clazz == null || interfaceType == null) {
            return result;
        }

        for (Type type : clazz.getGenericInterfaces()) {
            if (!(type instanceof ParameterizedType)) {
                continue;
            }

            ParameterizedType parameterizedType = ((ParameterizedType) type);
            if (!parameterizedType.getRawType().equals(interfaceType)) {
                continue;
            }

            for (Type actualTypeArgument : parameterizedType.getActualTypeArguments()) {
                if (actualTypeArgument instanceof Class) {
                    result.add((Class<?>) actualTypeArgument);
                }
            }
        }

        return result;
    }

    @NonNull
    public static Set<Class<?>> getInheritedInterfaces(Class<?> clazz) {
        if (clazz == null || clazz == Object.class) {
            return Collections.emptySet();
        }

        Set<Class<?>> result = new HashSet<>();

        for (Class<?> anInterface : clazz.getInterfaces()) {
            result.add(anInterface);
            result.addAll(getInheritedInterfaces(anInterface));
        }

        result.addAll(getInheritedInterfaces(clazz.getSuperclass()));

        return result;
    }

    @NonNull
    public static Set<Method> findDeclaredMethodsByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationType) {
        if (clazz == null || clazz == Object.class) {
            return Collections.emptySet();
        }

        Set<Method> result = new HashSet<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotation(annotationType) != null) {
                result.add(method);
            }
        }

        return result;
    }

    @Nullable
    public static <T extends Annotation> T getParameterAnnotation(Method method, int index, Class<T> annotationType) {
        Annotation[] annotations = method.getParameterAnnotations()[index];
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(annotationType)) {
                return annotationType.cast(annotation);
            }
        }

        return null;
    }
}
