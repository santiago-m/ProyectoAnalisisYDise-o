package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


@WebSocket
public class QuestionWebSocketHandler {
    public static List<Session> sessions = new ArrayList<>();
    private static Map<String, Session> usernameSession = new HashMap<String, Session>();

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

        String clientUsername = (String) data.get("username");

        if (usernameSession.get(clientUsername) == null) {
            usernameSession.put(clientUsername, user);

            System.out.println("Session de "+clientUsername+" agregada!");
        }

        if (data.get("answer") != null) {
            for (spark.Session s : App.openSessions) {
                if (s.attribute(App.SESSION_NAME).equals(clientUsername)) { 
                    manageAnswer(user, s, data);
                    break;
                }    
            }
        }
    }

    public void manageAnswer(Session client, spark.Session sess, Map message) {
        String username = (String) message.get("username");
        String opponent = (String) message.get("opponent");
        Double cantPlayers = (Double) message.get("cantPlayers");

        Double questionID = (Double) message.get("idPregunta");
        String answer = (String) message.get("answer");
        Double puntajeObtenido = (Double) message.get("puntaje");

        Map<String, Object> respuesta = new HashMap<String, Object>();

        spark.Session sparkSession;

        sparkSession = sess;

        if (Game.esCorrecta(questionID.intValue(), answer)) {
            //Map nextQuestion = User.obtenerPregunta(username);
            //nextQuestion.put("player", username);
            try {
                //client.getRemote().sendString(String.valueOf(new Gson().toJson(nextQuestion)));
                respuesta.put("puedeJugar", true);
                client.getRemote().sendString(new Gson().toJson(respuesta));
            }
            catch(java.io.IOException e) {
                System.out.println("Unable to send message. Error: "+e);
            }
        }
        else {

            if (cantPlayers.intValue() == 2) {
                try {
                    respuesta.put("puedeJugar", false);
                    client.getRemote().sendString(new Gson().toJson(respuesta));
                    if (usernameSession.get(opponent) != null) {
                        System.out.println("No es nulo!");
                    }
                    else {
                        System.out.println("Es nulo :(");
                    }
                    respuesta.put("puedeJugar", true);
                    respuesta.put("puntajeOponente", puntajeObtenido);
                    usernameSession.get(opponent).getRemote().sendString(new Gson().toJson(respuesta));
                }
                catch(java.io.IOException e) {
                    System.out.println("Unable to send message. Error: "+e);
                }   
            }
            else {
                try {
                    respuesta.put("puedeJugar", false);
                    client.getRemote().sendString(new Gson().toJson(respuesta));
                }
                catch(java.io.IOException e) {
                    System.out.println("Unable to send message. Error: "+e);
                }       
            }
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
