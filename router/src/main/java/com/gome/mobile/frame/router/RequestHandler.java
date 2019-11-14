package com.gome.mobile.frame.router;

import android.os.Bundle;

import com.gome.mobile.frame.router.intf.Result;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class RequestHandler {
    private final String serviceUri;
    private final Method method;

    public RequestHandler(String serviceUri, Method method) {
        if (!isAcceptableMethod(method)) {
            throw new RuntimeException(String.format("Method %s is not acceptable.", method.getName()));
        }
        this.serviceUri = serviceUri;
        this.method = method;
    }

    public Object handle(RouteService service, Bundle params, Result result) {
        method.setAccessible(true);
        try {
            return invokeMethod(service, params, result);
        } catch (Exception e) {
            result.error(null, "Error in invoking handler.", e);
        }

        return null;
    }

    public static boolean isAcceptableMethod(Method method) {
        for (Class<?> type : method.getParameterTypes()) {
            if (!type.equals(Bundle.class) && !type.equals(Result.class)) {
                return false;
            }
        }

        return true;
    }

    private Object invokeMethod(RouteService service, Bundle params, Result result)
            throws InvocationTargetException, IllegalAccessException {

        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] arguments = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; ++i) {
            if (paramTypes[i].equals(Bundle.class)) {
                arguments[i] = params;
            } else if (paramTypes[i].equals(Result.class)) {
                arguments[i] = result;
            }
        }

        method.setAccessible(true);
        return method.invoke(service, arguments);
    }

    public Method getMethod() {
        return method;
    }

    public String getServiceUri() {
        return serviceUri;
    }
}
