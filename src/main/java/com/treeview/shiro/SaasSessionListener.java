package com.treeview.shiro;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SaasSessionListener implements SessionListener {
    private static final ConcurrentHashMap<String, Session> SESSION_MAP = new ConcurrentHashMap<>();
    @Override
    public void onStart(Session session) {
        SESSION_MAP.put(String.valueOf(session.getId()), session);
    }

    @Override
    public void onStop(Session session) {
    }

    @Override
    public void onExpiration(Session session) {
    }
}
