package com.treeview.aspect;

import com.google.gson.Gson;
import com.treeview.annotation.Logit;
import com.treeview.entity.monitor.AppLogs;
import com.treeview.entity.system.UserInfo;
import com.treeview.service.system.AppLogsService;
import com.treeview.utils.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
@Slf4j
@Component
public class LogitAspect {
    @Resource
    @Qualifier("gsonInst")
    private Gson gson;

    @Resource
    private AppLogsService appLogsService;

    @Resource
    private ThreadPoolTaskExecutor serviceTaskExecutor;

    @Around("@annotation(logit)")
    public Object doAround(ProceedingJoinPoint joinPoint, Logit logit) throws Throwable {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            UserInfo userInfo = ShiroUtil.getSessionUser();
            if (logit != null) {
                serviceTaskExecutor.submit(() ->{
                    final AppLogs appLogs = new AppLogs();
                    appLogs.setCreateTime(new Date());
                    appLogs.setDetails(logit.value());
                    appLogs.setUsername(userInfo != null ? userInfo.getUserName() : "system");
                    appLogs.setUrl(request.getRequestURI());
                    appLogs.setParams(gson.toJson(request.getParameterMap()));
                    this.appLogsService.save(appLogs);
                });
            }
        } catch (Exception e) {
            log.error("Exception:", e);
        }

        Object obj = null;

        try {
            obj = joinPoint.proceed();
        } catch (Exception e) {
            log.error("Exception:", e);
        }

        return obj;
    }
}