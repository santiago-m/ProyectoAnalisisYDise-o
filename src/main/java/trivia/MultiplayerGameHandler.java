package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import com.google.gson.Gson;

@WebSocket
public class MultiplayerGameHandler {

    Map<String, String> hostNameOwner = new HashMap<String, String>();
    Map<String, Session> userSession = new HashMap<String, Session>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("\n ---> Connected to MultiplayerGameHandler <--- \n");
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        //sessions.remove(session);
        System.out.println("\n ---> Disconnected from MultiplayerGameHandler <--- \n");

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        System.out.println(msg);
        Map message = new Gson().fromJson(msg, Map.class);

        String hostOwner = (String) message.get("owner");
        String hostname = (String) message.get("hostname");

        System.out.println(hostOwner);
        System.out.println(hostname);
        
        if (hostOwner != null) {
            hostNameOwner.put(hostname, hostOwner);
            userSession.put(hostOwner, session);
        }
        else {
            try {
                hostOwner = hostNameOwner.get(hostname);
            }
            catch (NullPointerException e) {
                System.out.println("I'm sorry, cannot obtain hostOwner");
            }

            System.out.println("Obtuve creador del host " + hostname + ". Es " + hostOwner);

            try {
                userSession.get(hostOwner).getRemote().sendString("Can_Play");
            }
            catch (java.io.IOException e) {
                System.out.println("Cannot inform hostOwner that can play");
            }
        }
    }
}