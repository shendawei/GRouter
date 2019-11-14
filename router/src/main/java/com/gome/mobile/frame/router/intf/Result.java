package com.gome.mobile.frame.router.intf;

public interface Result {
    void success(Object obj);

    void error(String errorCode, String errorMessage, Throwable cause);

    void error(String errorMessage, Throwable cause);
}
