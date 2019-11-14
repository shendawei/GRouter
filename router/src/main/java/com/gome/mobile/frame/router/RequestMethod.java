package com.gome.mobile.frame.router;

public enum RequestMethod {

    Get("GET"), Post("POST"), Put("PUT"), Delete("DELETE");

    private String value;

    RequestMethod(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
