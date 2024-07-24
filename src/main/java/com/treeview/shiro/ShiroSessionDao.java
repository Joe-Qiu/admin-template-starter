package com.treeview.shiro;//

import com.treeview.service.framework.RedisService;
import com.treeview.utils.RedisSerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ShiroSessionDao extends AbstractSessionDAO {
    @Resource
    private RedisService redisService;

    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            log.error("session id is null");
            return null;
        } else {
            Session s = null;
            Object obj = this.redisService.get("shiro_redisdb_session:" + sessionId);
            if (obj != null) {
                s = RedisSerializeUtil.deserialize((byte[])obj);
            }

            return s;
        }
    }

    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    private void saveSession(Session session) throws UnknownSessionException {
        if (session != null && session.getId() != null) {
            if (session != null) {
                long expireTime = 1800000L;
                session.setTimeout(expireTime);
                this.redisService.setEx("shiro_redisdb_session:" + session.getId(), RedisSerializeUtil.serialize(session), expireTime, TimeUnit.MILLISECONDS);
            }
        } else {
            log.error("session or session id is null");
        }
    }

    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            this.redisService.del("shiro_redisdb_session:" + session.getId());
        } else {
            log.error("session or session id is null");
        }
    }

    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet();
        return sessions;
    }
}