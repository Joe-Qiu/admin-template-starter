package com.treeview.utils;

import com.treeview.entity.system.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class ShiroUtil {
    public static UserInfo getSessionUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Object object = subject.getPrincipal();
            if (object != null) {
                UserInfo userInfo = (UserInfo) object;
                return userInfo;
            }
        }

        return null;
    }

    public static Long getSessionUid() {
        UserInfo userInfo = getSessionUser();
        return userInfo != null ? userInfo.getId() : null;
    }

    public static String getRemoteAddress() {
        return StringUtils.substring(getSubject().getSession().getHost(), 0, 128);
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }
}