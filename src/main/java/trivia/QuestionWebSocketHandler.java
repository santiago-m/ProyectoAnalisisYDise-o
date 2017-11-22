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
    private static List<String> readyToPlay = new ArrayList<String>();
    private static Map<String, Integer> playersFinished = new HashMap<String, Integer>();

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        sessions.add(user);
        System.out.println("WebSocket iniciado en: "+user.getLocalAddress().toString());
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        sessions.remove(user);

        for (int i = 0; i < readyToPlay.size(); i++) {
            if (usernameSession.get(readyToPlay.get(i)).equals(user)) {
                usernameSession.remove(readyToPlay.get(i));
                readyToPlay.remove(i);
                break;
            }
        }
        System.out.println("closed");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {

        Map data = new Gson().fromJson(message, Map.class);

        String clientUsername = (String) data.get("username");
        String clientOpponent = (String) data.get("opponent");

        System.out.println("Recibi mensaje de " + clientUsername + " cuyo oponente es " + clientOpponent);

        if (usernameSession.get(clientUsername) == null) {
            usernameSession.put(clientUsername, user);

            System.out.println("Session de "+clientUsername+" agregada!");

            readyToPlay.add(clientUsername);

            for (int i = 0; i < readyToPlay.size(); i++) {
                System.out.println(readyToPlay.get(i));
            }

            if (readyToPlay.contains(clientOpponent)) {
                try {
                    user.getRemote().sendString("opponentReady");
                }
                catch (java.io.IOException e) {
                    System.out.println("Cannot tell " + clientUsername + " that the opponent " + clientOpponent + " is ready");
                }

                try {
                    usernameSession.get(clientOpponent).getRemote().sendString("opponentReady");
                }
                catch (java.io.IOException e) {
                    System.out.println("Cannot tell opponent of " + clientUsername + " that the player is ready");
                }
            }
            else {
                try {
                    user.getRemote().sendString("opponentNotReadyYet");
                }
                catch (java.io.IOException e) {
                    System.out.println("Cannot tell " + clientUsername + " that the opponent " + clientOpponent + " is not ready yet");
                }   
            }
        }

        if (data.get("answer") != null) {
            for (spark.Session s : App.openSessions) {
                if (s.attribute(App.SESSION_NAME).equals(clientUsername)) { 
                    manageAnswer(user, s, data);
                    break;
                }    
            }
        }
        else {
          if (data.get("IQuit") != null) {
            boolean quit = (boolean) data.get("IQuit");
            if (quit) {
              setResult(clientUsername, clientOpponent);
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

        boolean finished = (boolean) message.get("finished");
        boolean playingAlone = (boolean) message.get("alone");

        boolean quit = (boolean) message.get("IQuit");

        Map<String, Object> respuesta = new HashMap<String, Object>();

        spark.Session sparkSession;

        sparkSession = sess;
      if (quit) {
        setResult(username, opponent);
      }
      else {
        if (Game.esCorrecta(questionID.intValue(), answer)) {

            if (finished) {
                playersFinished.put(username, puntajeObtenido.intValue() + 5);
                checkResult(username, opponent);
            }
            else {
                try {
                    respuesta.put("puedeJugar", true);
                    client.getRemote().sendString(new Gson().toJson(respuesta));
                }
                catch(java.io.IOException e) {
                    System.out.println("Unable to send message. Error: "+e);
                }
            }
        }
        else {
            if (finished) {
                playersFinished.put(username, puntajeObtenido.intValue());
                checkResult(username, opponent);
            }
            else {
                if (cantPlayers.intValue() == 2) {
                    try {
                        respuesta.put("puedeJugar", false);
                        client.getRemote().sendString(new Gson().toJson(respuesta));

                        if (!playingAlone) {
                            respuesta.put("puedeJugar", true);
                            respuesta.put("puntajeOponente", puntajeObtenido);
                            usernameSession.get(opponent).getRemote().sendString(new Gson().toJson(respuesta));
                        }
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
      }  
    }

    private void setResult(String playerWhoQuit, String opponent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("playerState", "gaveUp");

        try {
          usernameSession.get(playerWhoQuit).getRemote().sendString(new Gson().toJson(temp));
        }
        catch(java.io.IOException e) {
          System.out.println("Could not inform player that lost because he abandoned");
        }

        temp.put("playerState", "opponentGaveUp");
        
        try {
          usernameSession.get(opponent).getRemote().sendString(new Gson().toJson(temp));
        }
        catch(java.io.IOException e) {
          System.out.println("Could not inform opponent that win because player abandoned");
        }      


        sessions.remove(usernameSession.get(playerWhoQuit));
        sessions.remove(usernameSession.get(opponent));

        usernameSession.remove(playerWhoQuit);
        usernameSession.remove(opponent);
    }

    private void checkResult(String player, String opponent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("puntaje_" + player, playersFinished.get(player));
        if (playersFinished.get(opponent) != null) {
            
            if (playersFinished.get(player) > playersFinished.get(opponent)) {
                temp.put("playerState", "winner");
            }
            else if (playersFinished.get(player) == playersFinished.get(opponent)) {
                temp.put("playerState", "draw");
            }
            else {
                temp.put("playerState", "loser");
            }

            try {
                usernameSession.get(player).getRemote().sendString(new Gson().toJson(temp));

                if (temp.get("playerState") == "winner") {
                    temp.put("playerState", "loser");
                }
                else if (temp.get("playerState") == "loser") {
                    temp.put("playerState", "winner");
                }

                usernameSession.get(opponent).getRemote().sendString(new Gson().toJson(temp));
            }
            catch (java.io.IOException e) {
                System.out.println("Could not inform winner or loser");
            }

            playersFinished.remove(player);
            playersFinished.remove(opponent);

            sessions.remove(usernameSession.get(player));
            sessions.remove(usernameSession.get(opponent));

            usernameSession.remove(player);
            usernameSession.remove(opponent);

            readyToPlay.remove(player);
            readyToPlay.remove(opponent);
        }
        else {
            temp.put("playerState", "waitOpponentToFinish");
            try {
                usernameSession.get(player).getRemote().sendString(new Gson().toJson(temp));
                System.out.println("Pudo enviar waitOpponentToFinish a " + player);
            }
            catch (java.io.IOException e) {
                System.out.println("Cannot tell " + player + "to wait for " + opponent);
            }

            temp.remove("playerState");
            temp.put("playerState", "opponentFinished");

            try {
                usernameSession.get(opponent).getRemote().sendString(new Gson().toJson(temp));
                System.out.println("Pudo enviar opponentFinished a " + opponent);
            }
            catch (java.io.IOException e) {
                System.out.println("Cannot tell " + opponent + "that " + player + "has finished playing");
            }
        }
    }

}
