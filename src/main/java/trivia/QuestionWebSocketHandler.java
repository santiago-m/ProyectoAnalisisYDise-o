package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

@WebSocket
public class QuestionWebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("conectado");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.println("closed");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println("mensaje recibido");
        System.out.println(message);
    }

}
