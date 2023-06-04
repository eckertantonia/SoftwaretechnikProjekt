package de.hsrm.mi.team3.swtp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/*
 * Websocket Configuration.
 * WebSocketMessageBrokerConfigurer is implemented.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  /**
   * Method sets configuration for all destinations in the registry.
   *
   * @param registry
   */
  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // Prefix für alle zugehörigen Destinations,
    // z.B. /topic/news, /topic/offers usw.
    registry.enableSimpleBroker("/topic");
  }

  /**
   * Method sets configuration for stomp ending point.
   *
   * @param registry
   */
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/stomp-broker").setAllowedOrigins("*");
  }
}
