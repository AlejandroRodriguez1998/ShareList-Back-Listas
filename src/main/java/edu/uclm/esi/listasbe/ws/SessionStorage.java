package edu.uclm.esi.listasbe.ws;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SessionStorage {
    private final Map<String, List<WebSocketSession>> sessionsByIdLista = new ConcurrentHashMap<>();

    public Map<String, List<WebSocketSession>> getSessionsByIdLista() {
        return sessionsByIdLista;
    }
}
