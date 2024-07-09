package com.treeview.enums.system.user;

import lombok.Getter;
import lombok.Setter;

public enum UserStatusEnum {
    EFF_USER("1"),
    EXP_USER("0");

    UserStatusEnum(String status) {
        this.status = status;
    }

    @Getter
    @Setter
    private String status;
}
