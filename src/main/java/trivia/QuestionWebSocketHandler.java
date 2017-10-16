package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;


@WebSocket
public class QuestionWebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("conectado");
        System.out.println(user.getRemote().toString());
        List cookies = user.getUpgradeRequest().getCookies();
        System.out.println("Handler!");
        System.out.println("Handler!");
        System.out.println(cookies.toString());
        System.out.println("Handler!");
        System.out.println("Handler!");

        //sendMessage(user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.println("closed");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println("mensaje recibido");
        System.out.println(message);
        sendMessage(user);
    }

    public void sendMessage(Session sesion) {
        if (sesion.isOpen()) {
            try {
                sesion.getRemote().sendString(String.valueOf(new JSONObject()
                    .put("sesion", sesion)
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
