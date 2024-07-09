package com.treeview.enums.system.user;

import lombok.Getter;
import lombok.Setter;

public enum UserTypeEnum {
    OUTER_USER("01"),
    INNER_USER("00");

    UserTypeEnum(String type) {
        this.type = type;
    }

    @Getter
    @Setter
    private String type;
}
