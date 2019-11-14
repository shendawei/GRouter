package com.gome.mobile.frame.router.intf;

public interface RequestCallback {
    void onSuccess(Object value);

    void onError(String errorCode, String errorMessage, Throwable cause);
}
