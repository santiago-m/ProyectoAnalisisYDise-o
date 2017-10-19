package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class busqueda {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Conexion a busqueda");
        //pedir a host_creado // hosts

    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        //sessions.remove(session);
        System.out.println("Desconexion de espera");

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        
    }


}