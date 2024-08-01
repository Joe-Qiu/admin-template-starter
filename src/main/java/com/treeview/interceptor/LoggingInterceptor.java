package com.treeview.interceptor;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
    private static Gson gson = new Gson();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            final Map<String, Object> params = new HashMap<>();

            params.put("Request URL", request.getRequestURL());
            params.put("Request Method", request.getMethod());
            params.put("Request Parameters", request.getParameterMap());

            if(log.isDebugEnabled()){
                log.debug(gson.toJson(params));
            }
        }
        return true;
    }
}
