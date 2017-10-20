package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


@WebSocket
public class QuestionWebSocketHandler {
    public static List<Session> sessions = new ArrayList<>();

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        sessions.add(user);
        System.out.println("WebSocket iniciado en: "+user.getLocalAddress().toString());
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

        Map data = new Gson().fromJson(message, Map.class);

        String clientUsername = (String) data.get("username");
        Double questionID = (Double) data.get("idPregunta");
        String answer = (String) data.get("answer");

        for (spark.Session s : App.openSessions) {
            try {
                if (s.attribute("clientAddress").equals(ipClient)) {
                    sparkSession = s;

                    if (Game.esCorrecta(questionID.intValue(), answer)) {
                        System.out.println("Es correcta!");
                    }
                    else {
                        System.out.println("No es Correcta!");
                    }

                    break;
                }
            }
            catch(Exception e) {
                if (s.attribute(App.SESSION_NAME).equals(clientUsername)) { 
                    s.attribute("clientAddress", ipClient);
                    sparkSession = s;

                    if (Game.esCorrecta(questionID.intValue(), answer)) {
                        System.out.println("Es correcta!");
                    }
                    else {
                        System.out.println("No es Correcta!");
                    }

                    break;
                }    
            }

            
        }

/*
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
                    System.out.println(sparkSession);
                    break;
                }
                else {
                    System.out.println((String) s.attribute("username"));
                }
            }
            System.out.println("mensaje recibido");
            System.out.println(message);
        }*/
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
