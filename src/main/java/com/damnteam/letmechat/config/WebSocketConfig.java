package com.damnteam.letmechat.config;

import com.damnteam.letmechat.service.CustomMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public WebSocketHandler customMessageHandler() {
        return new CustomMessageHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customMessageHandler(), "/myHandler");
    }

}
