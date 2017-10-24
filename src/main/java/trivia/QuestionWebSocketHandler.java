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
        Map data = new Gson().fromJson(message, Map.class);
        String ipClient = (user.getLocalAddress().toString()).substring(user.getLocalAddress().toString().indexOf(":"));

        String clientUsername = (String) data.get("username");

        for (spark.Session s : App.openSessions) {
            try {
                if (s.attribute("clientAddress").equals(ipClient)) {
                    manageAnswer(user, s, data);
                }
                break;
            }
            catch(Exception e) {
                if (s.attribute(App.SESSION_NAME).equals(clientUsername)) { 
                    s.attribute("clientAddress", ipClient);
                    manageAnswer(user, s, data);
                    break;
                }    
            }

            
        }
    }

    public void manageAnswer(Session client, spark.Session sess, Map message) {
        String username = (String) message.get("username");
        Double questionID = (Double) message.get("idPregunta");
        String answer = (String) message.get("answer");
        spark.Session sparkSession;

        sparkSession = sess;

        System.out.println(answer);

        if (Game.esCorrecta(questionID.intValue(), answer)) {
            Map nextQuestion = User.obtenerPregunta(username);
            //nextQuestion.put("player", username);
            try {
                client.getRemote().sendString(String.valueOf(new Gson().toJson(nextQuestion)));    
            }
            catch(java.io.IOException e) {
                System.out.println("Unable to send message. Error: "+e);
            }
            
        }
        else {
            System.out.println("No es Correcta!");
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
