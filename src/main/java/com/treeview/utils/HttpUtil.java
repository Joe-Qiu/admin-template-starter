package com.treeview.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author joe.qiu on 2020/03/06
 * @version V1.0.0
 */
public class HttpUtil {
    public static boolean isGet(HttpServletRequest request) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        return false;
    }

    public static boolean isPost(HttpServletRequest request) {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        return false;
    }
}