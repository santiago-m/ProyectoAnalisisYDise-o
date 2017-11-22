package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import org.json.JSONObject;

@WebSocket
public class busqueda {

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("\n ---> Connected to busqueda <--- \n");
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("\n ---> Disconnected from busqueda <--- \n");

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        if (msg.equals("p_partidas")){
            sendGames(session);
        } else {
            System.out.println("segundo del if"); // "posible implementacion" (que nunca va a llegar (?))
            //App.startGame(sender = App.concurr.get(session), msg);
            // aca necesito el usuario correcto.. no User1
            // msg es el nombre de la partida
        }
    }


    public static void sendGames(Session session) {

      int juegos_totales = (int) App.hosts.get("cantidadHosts");
      List<String> usuario = new ArrayList<String>();
      List<String> nombre_partida = new ArrayList<String>();
      List<Integer> cPreguntas = new ArrayList<>();
      String aux;

      for (int i= 1; i<(juegos_totales+1) ; i++ ) {
        aux = (String) App.hosts.get("Host "+ (i));

        nombre_partida.add(aux);
        usuario.add(((User) App.hostUser.get(aux)).getUsername());
        cPreguntas.add((int) App.hosts.get(aux));
      }
      try {

        session.getRemote()
               .sendString(String.valueOf(new JSONObject().put("usuario", usuario)
                                                          .put("nombre_partida", nombre_partida)
                                                          .put("cPreguntas", cPreguntas)
                                                          ));

      } catch (Exception r){
        r.printStackTrace();
      }

    }


}
