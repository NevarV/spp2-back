package com.spp2.service.backproject.security.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/auth/**").permitAll()
                .simpSubscribeDestMatchers("/user/queue/auth/reply").permitAll()
                .simpMessageDestMatchers("/users/filter", "/users").hasRole("READ")
                .simpMessageDestMatchers("/users/{\\d+}/delete", "/users/edit", "/users/{\\d+}", "/users/new").hasRole("WRITE")
                .simpTypeMatchers(SUBSCRIBE).hasRole("READ")
                .anyMessage().permitAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

}
