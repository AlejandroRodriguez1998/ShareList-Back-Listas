package edu.uclm.esi.listasbe.ws;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class WsChat extends TextWebSocketHandler {
	
	private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	private Map<String, WebSocketSession> sessionsByNombre = new ConcurrentHashMap<>();
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println(session.getId()); //Este metodo es cuando invocas la conexion 
		String nombreUsuario = this.getNombreParameter(session);
		this.sessions.put(session.getId(), session);
		this.sessionsByNombre.put(nombreUsuario, session);
		
		this.sessions.put(session.getId(), session);
		JSONObject jso = new JSONObject();
		jso.put("tipo", "llegadaDeUsuario");
		jso.put("contenido", nombreUsuario);
		this.difundir(jso);
	}
	
	private String getNombreParameter(WebSocketSession session) {
		URI uri = session.getUri();
		String query = uri.getQuery();
		for (String param : query.split("&")) {
			String[] pair = param.split("=");
			if (pair.length > 1 && "nombre".equals(pair[0])) {
				return pair[1];
			}
		}
		return null;
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		JSONObject jso = new JSONObject();
		if (jso.getString("tipo").equalsIgnoreCase("difusion")) {
			jso.put("tipo", "mensajeTexto");
			jso.put("contenido", jso.getString("contenido"));
			this.difundir(jso);
			
		} else if (jso.getString("tipo").equalsIgnoreCase("mensajeParticular")) {
			String destinatario = jso.getString("destinatario");
			WebSocketSession wsDestinatario = this.sessionsByNombre.get(destinatario);
			if (wsDestinatario != null) {
				wsDestinatario.sendMessage(message);
			}
			
		} // Si no, podríamos o no hacer nada o mandar un error. Por comodidas, hacemos lo primero
		
		
		jso.put("tipo", "mensajeTexto");
		jso.put("contenido", message.getPayload());
		this.difundir(jso);
	}
	
	private void difundir(JSONObject jso) throws IOException {
		TextMessage message = new TextMessage(jso.toString());
		
		for (WebSocketSession target : this.sessions.values()) {
			//Creamos un hilo para cada target, para cada destinatario del mensaje
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						target.sendMessage(message);
					} catch (IOException e) {
						//Si se produce una excepción, se elimina la sesisón de la lista de sesiones
						//No se puede poner solo this porque aqui el ambito de this es runnable
						//Tenemos que acceder así a las sesiones
						WsChat.this.sessions.remove(target.getId());
					}
				}
				
			}).start();
			

		}
		
	}
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		this.sessions.remove(session.getId());
	}
	


	
	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
		//Cuando pasamos al mensaje un archivo
	}
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		//Hay un error en el websocket cuando se ejecuta este metodo
	}
}