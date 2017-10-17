package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class edicionPreguntas {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Conexion a edicionPreguntas");
        //System.out.println("Session= " + session + "<End Session>");

        //String username = App.SESSION_NAME;   // <-- SESSION_NAME es privado

        String username = "User" + App.nextUserNumber++;
        App.concurr.put(session, username);
        //System.out.println("Session: " + session + "username" + username);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        //sessions.remove(session);
        System.out.println("Desconexion de edicionPreguntas");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String buscar) throws IOException {

        try{
            App.sendQuestions(sender = App.concurr.get(session), msg = buscar);
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}