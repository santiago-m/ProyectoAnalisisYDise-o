package trivia;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DBException;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class preguntas {

    private String sender, msg;
    // Store sessions if you want to, for example, broadcast a message to all users
    //private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Session= " + session + "<End Session>");

        String username = "User" + App.nextUserNumber++;
        App.concurr.put(session, username);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        //sessions.remove(session);
        System.out.println("Bye!3");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String buscar) throws IOException {
        /*App.openDB();
        List<Question> cambiar = Question.where("pregunta = "+ buscar);
        App.closeDB();
        */
        System.out.println("weeenas");
        App.sendQuestions(sender = App.concurr.get(session), msg = buscar);
        System.out.println("weeenas2");
    }


}