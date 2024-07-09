package com.treeview.enums;

import lombok.Getter;
import lombok.Setter;

public enum LangEnum {
    EN_US("en_US", "English"),
    ZH_CN("zh_CN", "中文");

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String name;

    LangEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}