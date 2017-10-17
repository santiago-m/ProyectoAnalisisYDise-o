package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class WebSocketHandler2 {

    // Store sessions if you want to, for example, broadcast a message to all users
    //private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("asd2");
        //sessions.add(session);
    }

    @OnWebSocketClose
    public void onclose(Session session, int statusCode, String reason) {
        //sessions.remove(session);
        System.out.println("Bye!2");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("Got2: " + message);   // Print message
        //session.getRemote().sendString(message); // and send it back}
    }


}