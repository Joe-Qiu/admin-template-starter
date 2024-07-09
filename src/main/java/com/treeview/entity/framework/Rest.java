package com.treeview.entity.framework;

import lombok.Data;

@Data
public class Rest<T> {
    private long code = 0;

    private T data;

    private String msg;

    public Rest() {
        super();
    }

    public Rest(long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Rest(long code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static Rest ok() {
        return new Rest(200, null, "");
    }

    public static Rest ok(String msg) {
        return new Rest(200, null, msg);
    }

    public static Rest okData(Object data) {
        return new Rest(200, data, "ok");
    }

    public static Rest failure(String msg) {
        return new Rest(500, null, msg);
    }
}