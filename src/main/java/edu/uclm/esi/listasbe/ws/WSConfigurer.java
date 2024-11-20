package edu.uclm.esi.listasbe.ws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WSConfigurer implements WebSocketConfigurer {
	
	private final WsChat wsChat;
    private final WsListas wsListas;

    @Autowired
    public WSConfigurer(WsChat wsChat, WsListas wsListas) {
        this.wsChat = wsChat;
        this.wsListas = wsListas;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(wsChat, "/wsChat").setAllowedOrigins("*")
                .addHandler(wsListas, "/wsLista")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }
}