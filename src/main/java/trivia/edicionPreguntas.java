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
        //System.out.println("Conexion a edicionPreguntas");    // debug
        String username = "User" + App.nextUserNumber++;    // ATM works ;)
        App.concurr.put(session, username);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        App.concurr.remove(session);
        //System.out.println("Desconexion de edicionPreguntas");    //debug
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String buscar) throws IOException {

        try{
            App.editQuestions(sender = App.concurr.get(session), msg = buscar);
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}