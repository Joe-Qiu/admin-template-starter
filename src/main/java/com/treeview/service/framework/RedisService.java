package com.treeview.service.framework;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    void set(String key, Object value);

    void setEx(String key, Object value, long expireTime, TimeUnit timeUnit);

    Object get(String key);

    Boolean del(String key);
}