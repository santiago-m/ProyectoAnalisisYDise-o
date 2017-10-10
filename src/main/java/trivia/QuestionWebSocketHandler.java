package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import com.google.gson.Gson;
import org.json.JSONObject;


@WebSocket
public class QuestionWebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("conectado");
        
        sendMessage(user, "hola");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.println("closed");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println("mensaje recibido");
        System.out.println(message);
        sendMessage(user, message);
    }

    public void sendMessage(Session sesion, String msg) {
        if (sesion.isOpen()) {
            try {
                sesion.getRemote().sendString(String.valueOf(new JSONObject()
                    .put("sesion", sesion.toString())
                ));    
                System.out.println("La sesion esta abierta");
            }
            catch(Exception e) {
                System.out.println("error: "+e);
            }
            
        }
        else {
            System.out.println("Sesion cerrada!");
        }
    }

}
