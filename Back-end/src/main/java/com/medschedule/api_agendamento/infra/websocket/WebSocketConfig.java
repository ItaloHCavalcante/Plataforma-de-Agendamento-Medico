package com.medschedule.api_agendamento.infra.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita um message broker simples em memória para enviar mensagens de volta ao cliente
        config.enableSimpleBroker("/topic");
        // Prefixo para mensagens enviadas DO cliente PARA o servidor (se necessário)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Ponto de entrada onde o frontend vai se conectar
        registry.addEndpoint("/ws-medschedule").setAllowedOriginPatterns("*").withSockJS();
    }
}