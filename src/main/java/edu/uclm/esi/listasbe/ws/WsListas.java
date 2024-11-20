package edu.uclm.esi.listasbe.ws;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import edu.uclm.esi.listasbe.dao.ListaDao;
import edu.uclm.esi.listasbe.model.Producto;
import jakarta.annotation.PostConstruct;

@Component
public class WsListas extends TextWebSocketHandler {
	
	@Autowired
	private static ListaDao listaDao;
	
	private final SessionStorage sessionStorage;

    @Autowired
    public WsListas(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }
	
	@Autowired
	public void setListaDao(ListaDao listaDao) {
		WsListas.listaDao = listaDao;
	}
	
	@Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String email = this.getParameter(session, "email");

        List<String> listas = WsListas.listaDao.getListasDe(email);

        synchronized (this.sessionStorage.getSessionsByIdLista()) {
            for (String idLista : listas) {
                this.sessionStorage.getSessionsByIdLista()
                    .computeIfAbsent(idLista, k -> new ArrayList<>())
                    .add(session);
            }
        }
    }

    public void notificar(String idLista, Producto producto, String tipo) {
        synchronized (this.sessionStorage.getSessionsByIdLista()) {
            List<WebSocketSession> interesados = this.sessionStorage.getSessionsByIdLista().get(idLista);
            
            if (interesados != null) {
            	JSONObject jso = new JSONObject();
        		jso.put("tipo", tipo);
        		jso.put("idLista", idLista);
        		
            	if (tipo != "borradoLista") {
            		jso.put("idProducto", producto.getId());
        			jso.put("nombre", producto.getNombre());
        			jso.put("udsPedidas", producto.getUdsPedidas());
        			jso.put("udsCompradas", producto.getUdsCompradas());
            	} 
            
    			TextMessage message = new TextMessage(jso.toString());
    			
    			for (WebSocketSession target: interesados) {
    				new Thread(new Runnable() {
    					@Override
    					public void run() {
    						try {
    							target.sendMessage(message);
    						} catch (IOException e) {
    							//WsListas.this.sessions.remove(target.getId());
    						}
    					}
    				}).start();
    			}
            }
        }
    }
	
	private String getParameter(WebSocketSession session, String parameter) {
		URI uri = session.getUri();
		String query = uri.getQuery();
		
		for(String param: query.split("&")) {
			String [] pair = param.split("=");
			
			if(pair.length > 1 && parameter.equals(pair[0])) {
				return pair[1];
			}
		}
		
		return null;
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		//Hay un error en el websocket cuando se ejecuta este metodo
	}

}