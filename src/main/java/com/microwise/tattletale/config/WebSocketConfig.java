package com.microwise.tattletale.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.security.Principal;
import java.util.concurrent.ExecutionException;

/**
 * WebSocket 配置（整合RabbitMQ）
 *
 * @author li.jianfei
 * @since 2017/12/11
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Autowired
    private RabbitProperties rabbitProperties;
    @Autowired
    private SocketSessionRegistry socketSessionRegistry;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        //从配置文件读取RabbitMQ信息
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(rabbitProperties.getHost())
                .setRelayPort(61613)
                .setVirtualHost(rabbitProperties.getVirtualHost())
                .setSystemLogin(rabbitProperties.getUsername())
                .setSystemPasscode(rabbitProperties.getPassword());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/tattletale")
                //允许跨域访问
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String userId = accessor.getFirstNativeHeader("userId");
                    String sessionId = accessor.getSessionId();
                    Principal principal = new WebSocketPrincipal(userId, sessionId);
                    accessor.setUser(principal);
                    //添加缓存
                    try {
                        socketSessionRegistry.registerSessionId(userId, sessionId);
                    } catch (ExecutionException e) {
                        logger.error("添加缓存异常", e);
                    }
                } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    String sessionId = accessor.getSessionId();
                    //清除缓存
                    try {
                        socketSessionRegistry.unregisterSessionId(sessionId);
                    } catch (ExecutionException e) {
                        logger.error("清除缓存异常", e);
                    }
                }
                return message;
            }
        });
    }
}
