package com.microwise.tattletale.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;

/**
 * 用户session记录类
 *
 * @author sun.cong
 * @create 2018-01-05 9:58
 **/
@Component
public class SocketSessionRegistry {
    //存放所有session
    private final Cache<String, Set<String>> cache = CacheBuilder.newBuilder().build();

    public SocketSessionRegistry() {
    }

    /**
     * 获取sessionIds
     *
     * @param userId
     * @return
     */
    public Set<String> getSessionIds(String userId) throws ExecutionException {
        Set set = this.cache.getIfPresent(userId);
        return set != null ? set : Collections.emptySet();
    }

    /**
     * 获取所有session
     *
     * @return
     */
    public Cache<String, Set<String>> getAllSessionIds() {
        return this.cache;
    }

    /**
     * register session
     *
     * @param userId
     * @param sessionId
     */
    public void registerSessionId(String userId, String sessionId) throws ExecutionException {
        Assert.notNull(userId, "User must not be null");
        Assert.notNull(sessionId, "Session ID must not be null");
        Object set = this.cache.getIfPresent(userId);
        if (set == null) {
            set = new CopyOnWriteArraySet();
            this.cache.put(userId, (Set<String>) set);
        }
        ((Set) set).add(sessionId);
    }

    /**
     * unregister session
     *
     * @param sessionId
     * @throws ExecutionException
     */
    public void unregisterSessionId(String sessionId) throws ExecutionException {
        Assert.notNull(sessionId, "Session ID must not be null");
        ConcurrentMap<String, Set<String>> concurrentMap = this.cache.asMap();
        Set<String> userIds = concurrentMap.keySet();
        for (String userId : userIds) {
            Set<String> sessionIds = concurrentMap.get(userId);
            if (sessionIds != null && sessionIds.remove(sessionId) && sessionIds.isEmpty()) {
                this.cache.invalidate(userId);
            }
        }
    }
}
