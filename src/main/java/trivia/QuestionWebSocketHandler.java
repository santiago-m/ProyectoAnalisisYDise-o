package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;


@WebSocket
public class QuestionWebSocketHandler {
    public static List<Session> sessions = new ArrayList<>();

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        sessions.add(user);
        System.out.println("WebSocket iniciado en: "+user.getLocalAddress().toString());

        //sendMessage(user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        sessions.remove(user);
        System.out.println("closed");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        spark.Session sparkSession;
        String ipClient = (user.getLocalAddress().toString()).substring(user.getLocalAddress().toString().indexOf(":"));

        if (message.startsWith("username: ")) {
            System.out.println(message.lastIndexOf("username: "));

            for (spark.Session s : App.openSessions) {
                if ((s.attribute(App.SESSION_NAME).equals(message.substring(message.lastIndexOf("username: "))))) { 
                    s.attribute("clientAddress", ipClient);
                    sparkSession = s;
                    break;
                }
            }
        }
        else {
            for (spark.Session s : App.openSessions) {
                if (s.attribute("clientAddress").equals(ipClient)) {
                    sparkSession = s;
                    break;
                }
            }
            System.out.println("mensaje recibido");
            System.out.println(message);
            sendMessage(user);
        }
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
