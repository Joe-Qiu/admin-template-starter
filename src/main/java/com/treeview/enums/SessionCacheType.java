package com.treeview.enums;

import lombok.Getter;
import lombok.Setter;

public enum SessionCacheType {
    MEMORY("memory"),
    REDIS("redis");

    @Getter
    @Setter
    private String type;

    SessionCacheType(String type) {
        this.type = type;
    }
}