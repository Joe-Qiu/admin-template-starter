package com.treeview.shiro;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义任务调度器
 */
@Slf4j
@Data
@Component
public class TemplateSessionValidationScheduler implements SessionValidationScheduler {
    @Resource
    private ScheduledThreadPoolExecutor scheduledThreadPoolScheduler;

    /**
     * 会话验证管理器
     */
    @Lazy
    @Resource
    private ValidatingSessionManager sessionManager;

    private volatile boolean enabled = false;


    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void enableSessionValidation() {
        try {
            scheduledThreadPoolScheduler.scheduleAtFixedRate(() -> {
                if (enabled) {
                    sessionManager.validateSessions();
                }
            }, 1000, 10 * 60 * 1000, TimeUnit.MILLISECONDS);

            this.enabled = true;

            if (log.isDebugEnabled()) {
                log.debug("Session validation job successfully scheduled with Spring Scheduler.");
            }
        } catch (Exception e) {
            log.error("Error starting the Spring Scheduler session validation job.  Session validation may not occur.", e);
        }
    }

    @Override
    public void disableSessionValidation() {
        if (log.isDebugEnabled()) {
            log.debug("Stopping Spring Scheduler session validation job...");
        }

        scheduledThreadPoolScheduler.shutdown();

        this.enabled = false;
    }
}