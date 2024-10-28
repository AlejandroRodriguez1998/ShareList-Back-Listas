package edu.uclm.esi.listasbe.ws;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Component
public class wsChat extends TextWebSocketHandler {
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println(session.getId()); //Este metodo es cuando invocas la conexion 
	}
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println(); //Se ejecuta cuando un cliente manda un mensaje por websocket
		session.sendMessage(new TextMessage("Mensaje recibido")); // Envia un mensaje al cliente mediante la session
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