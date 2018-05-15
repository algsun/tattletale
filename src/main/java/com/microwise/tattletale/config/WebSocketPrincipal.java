package com.microwise.tattletale.config;

import java.security.Principal;

/**
 * Created by lijianfei on 2018/1/5.
 *
 * @author li.jianfei
 * @since 2018/1/5
 */
public class WebSocketPrincipal implements Principal {


    private String userId;
    private String sessionId;

    public WebSocketPrincipal(String userId, String sessionId) {
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    public String getName() {
        return sessionId;
    }
}
