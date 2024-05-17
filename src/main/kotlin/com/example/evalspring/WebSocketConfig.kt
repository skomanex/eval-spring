package com.example.evalspring

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration //Création d'un fichier de configuration pour les WebSocket
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    // Le point de terminaison pour les connexions WebSocket (handshake)
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("ws/websocket").setAllowedOriginPatterns("*").withSockJS()
    }
//    fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/matchesJson").allowedOrigins("*").allowedMethods("*")
//    }

    // Configurer les channels (broker) de messages que les clients viendront écouter
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        //Il peut y avoir plusieurs channels
        registry.enableSimpleBroker("/topic")
        //Définir les préfixes des destinations
        registry.setApplicationDestinationPrefixes("/ws")
    }
}
