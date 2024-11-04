package edu.uclm.esi.listasbe.ws;
import java.io.IOException;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import edu.uclm.esi.listasbe.dao.ListaDao;
import edu.uclm.esi.listasbe.model.Lista;
import edu.uclm.esi.listasbe.model.Producto;

@Component
public class WsListas extends TextWebSocketHandler {
	
	@Autowired
	private ListaDao listaDao;
	
	private Map<String, List<WebSocketSession>> sessionsByIdLista = new ConcurrentHashMap<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception { //Este metodo es cuando invocas la conexion 
		String email = this.getParameter(session,"email");
		
		List<String> listas = this.listaDao.getListasDe(email);
		
		for (String idLista : listas) {
			List<WebSocketSession> auxiliar = this.sessionsByIdLista.get(idLista);
			
			if (auxiliar == null) {
				auxiliar = new ArrayList<>();
				auxiliar.add(session);
			}else {
				auxiliar.add(session);
			}
			
			this.sessionsByIdLista.put(idLista, auxiliar);
		}
	}
	
	public void notificar(String idLista, Producto producto) {
		List<WebSocketSession> interesados = this.sessionsByIdLista.get(idLista);
		
		JSONObject jso = new JSONObject();
		jso.put("tipo","actualizacion");
		jso.put("idLista", idLista);
		jso.put("unidadesCompradas", producto.getUdsCompradas());
		jso.put("unidadesPedidas", producto.getUdsPedidas());
		jso.put("nombreProducto", producto.getNombre());
		
		TextMessage message = new TextMessage(jso.toString());
		
		for (WebSocketSession target: interesados) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						target.sendMessage(message); // Envia un mensaje al cliente mediante la session.
					} catch (IOException e) {
						//WsListas.this.sessions.remove(target.getId());
					}
				}
			}).start();
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
	
	private void difundir(JSONObject jso) throws IOException  {
		TextMessage message = new TextMessage(jso.toString());
		
		/*for (WebSocketSession target: this.sessions.values()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						target.sendMessage(message); // Envia un mensaje al cliente mediante la session.
					} catch (IOException e) {
						WsListas.this.sessions.remove(target.getId());
					}
				}
			}).start();
		}*/
		
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		//this.sessions.remove(session.getId());
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		//Hay un error en el websocket cuando se ejecuta este metodo
	}

}