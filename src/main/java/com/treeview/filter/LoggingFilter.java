package com.treeview.filter;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoggingFilter extends PathMatchingFilter {
    private static Gson gson = new Gson();

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        final Map<String, Object> params = new HashMap<>();

        params.put("Request URL", httpRequest.getRequestURL());
        params.put("Request Method", httpRequest.getMethod());
        params.put("Request Parameters", httpRequest.getParameterMap());

        if(log.isDebugEnabled()){
            log.debug(gson.toJson(params));
        }
        return true;
    }
}