package com.treeview.event;

import com.treeview.entity.system.UserInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {
    @Setter
    @Getter
    private UserInfo userInfo;

    public UserRegistrationEvent(Object source, UserInfo userInfo) {
        super(source);
        this.userInfo = userInfo;
    }
}