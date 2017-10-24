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
        System.out.println("1-" +session.getLocalAddress​() );
        System.out.println("2-" +session.getPolicy() );
        System.out.println("3-" +session.getRemote() );
        System.out.println("4-" +session.getRemoteAddress() );
        System.out.println("5-" +session.getUpgradeRequest() );
        System.out.println("6-" +session.getUpgradeResponse​() );
        System.out.println("7-" +session);
        String username = "User" + App.nextUserNumber++;    // ATM works ;)
        App.concurr.put(session, username);

    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        //sessions.remove(session);
        System.out.println("Desconexion de busqueda");

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        if (msg.equals("p_partidas")){
            App.sendGames(sender = App.concurr.get(session));
        } else {
            System.out.println("segundo del if");
            App.startGame(sender = App.concurr.get(session), msg);  
            // aca necesito el usuario correcto.. no User1
            // msg es el nombre de la partida
        }
    }


}