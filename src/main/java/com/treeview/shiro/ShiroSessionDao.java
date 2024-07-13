package com.treeview.shiro;//

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class ShiroSessionDao extends AbstractSessionDAO {
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    protected Session doReadSession(Serializable sessionId) {
//        if (sessionId == null) {
//            log.error("session id is null");
//            return null;
//        } else {
//            Session s = null;
//            byte[] value = this.redisClient.get(("shiro_redisdb_session:" + sessionId).getBytes());
//            if (value != null) {
//                s = (Session)RedisSerializeUtil.deserialize(value);
//            }
//
//            return s;
//        }
        return null;
    }

    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    private void saveSession(Session session) throws UnknownSessionException {
        if (session != null && session.getId() != null) {
            if (session != null) {
                long expireTime = 1800000L;
                session.setTimeout(expireTime);
//                this.redisClient.setEx(("shiro_redisdb_session:" + session.getId()).getBytes(), RedisSerializeUtil.serialize(session), expireTime, TimeUnit.MILLISECONDS);
            }

        } else {
            log.error("session or session id is null");
        }
    }

    public void delete(Session session) {
        if (session != null && session.getId() != null) {
//            this.redisClient.del("shiro_redisdb_session:" + session.getId());
        } else {
            log.error("session or session id is null");
        }
    }

    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet();
        return sessions;
    }
}