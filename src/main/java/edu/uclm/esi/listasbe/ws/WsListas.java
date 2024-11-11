package edu.uclm.esi.listasbe.ws;

import edu.uclm.esi.listasbe.dao.ListaDao;
import edu.uclm.esi.listasbe.model.Producto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class WsListas extends TextWebSocketHandler {

	private Map<String, List<WebSocketSession>> sessionsByIdLista = new ConcurrentHashMap<>();

	private static ListaDao listaDao;

	@Autowired //Esto lo ejecuta solo Spring cuando arranca
	public void setListaDao(ListaDao listaDao) {
		WsListas.listaDao = listaDao;
	}





	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println(session.getId()); //Este metodo es cuando invocas la conexion 
		String nombreUsuario = this.getNombreParameter(session);

		//Añadimos la sesión a la lista de sesiones
		List<String> listas = this.listaDao.getListasDe(nombreUsuario);

		for (String idLista : listas) {
			//Miro si no existe la lista
			List<WebSocketSession> auxi = this.sessionsByIdLista.get(idLista);
			if (auxi == null) { // Si no existe la lista, creo la lista que va a contener las sesiones
				auxi = new ArrayList<>();
				auxi.add(session);
			} else { // Si existe la lista, añado la sesión a la lista
				auxi.add(session);
			}

			this.sessionsByIdLista.put(idLista, auxi);
		}
	}

	public void notificar(String idLista, Producto producto) throws IOException {
		JSONObject jso = new JSONObject();
		jso.put("tipo", "actualizacionDeLista");
		jso.put("idLista", idLista);
		jso.put("unidadesCompradas", producto.getUdsCompradas());
		jso.put("unidadesPedidas", producto.getUdsPedidas());
		jso.put("nombreProducto", producto.getNombre());

		TextMessage message = new TextMessage(jso.toString());
		List<WebSocketSession> interesados = this.sessionsByIdLista.get(idLista);

		for (WebSocketSession target : interesados) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						target.sendMessage(message);
					} catch (IOException e) {
						WsListas.this.sessionsByIdLista.remove(target.getId());
					}
				}
			}).start();

		}
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
						WsListas.this.sessions.remove(target.getId());
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