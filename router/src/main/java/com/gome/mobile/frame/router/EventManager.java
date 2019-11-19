package com.gome.mobile.frame.router;

import android.os.Bundle;

import com.gome.mobile.frame.router.annotation.RouteEvent;
import com.gome.mobile.frame.router.utils.ReflectUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class EventManager {
    private Map<String, Set<EventHandler>> handlerMap = new HashMap<>();

    public void register(Object receiver) {
        Set<Method> methods = ReflectUtil.findDeclaredMethodsByAnnotation(receiver.getClass(), RouteEvent.class);
        for (Method method : methods) {
            RouteEvent annotation = method.getAnnotation(RouteEvent.class);
            Set<EventHandler> handlers = handlerMap.get(annotation.uri());
            if (handlers == null) {
                handlers = new HashSet<>();
            }

            handlers.add(new EventHandler(receiver, method, annotation.threadMode()));
            handlerMap.put(annotation.uri(), handlers);
        }
    }

    public void unregister(Object receiver) {
        Iterator<Entry<String, Set<EventHandler>>> entries = handlerMap.entrySet().iterator();
        while (entries.hasNext()) {
            Set<EventHandler> handlers = entries.next().getValue();
            Iterator<EventHandler> it = handlers.iterator();
            while (it.hasNext()) {
                if (it.next().getReceiver() == receiver) {
                    it.remove();
                }
            }
            if (handlers.isEmpty()) {
                entries.remove();
            }
        }
    }

    public void onEvent(String uri, Bundle params) {
        Set<EventHandler> handlers = handlerMap.get(uri);
        if (handlers != null) {
            for (EventHandler handler : handlers) {
                handler.handle(params);
            }
        }
    }
}
