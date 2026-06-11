package com.medschedule.api_agendamento.infra.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Esta anotação é crucial
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Define o endpoint de conexão para o WebSocket
        registry.addEndpoint("/ws-medschedule")
                .setAllowedOrigins("http://localhost:5173") // Permite a origem do seu front-end
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Define o prefixo para os endpoints que recebem mensagens dos clientes
        registry.setApplicationDestinationPrefixes("/app");
        // Habilita um "broker" de mensagens simples para os canais /topic e /queue
        // É isso que disponibiliza o SimpMessagingTemplate
        registry.enableSimpleBroker("/topic", "/queue");
    }
}
