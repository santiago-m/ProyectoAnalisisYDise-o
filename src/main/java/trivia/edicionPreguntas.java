package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import org.json.JSONObject;

@WebSocket
public class edicionPreguntas {

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("\n ---> Connected to WebSocket edicionPregntas <--- \n");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.println("\n ---> Disconnected from WebSocket edicionPregntas <--- \n");
    }

    // Se hace una consulta a la base de datos en base al pedido y
    // devuelve una lista con las preguntas y sus respectivos ID
    @OnWebSocketMessage
    public void onMessage(Session session, String buscar) throws IOException {

        App.openDB();
        List<Question> cambiar = Question.where("pregunta like '%"+buscar+"%'");

        List<Integer> id = new ArrayList<Integer>();
        List<String> preguntas = new ArrayList<String>();
        /*  Aun no implementado como quiero
        List<String> correcta = new ArrayList<String>();
        List<String> mal1 = new ArrayList<String>();
        List<String> mal2 = new ArrayList<String>();
        List<String> mal3 = new ArrayList<String>();
        List<String> activa = new ArrayList<String>();*/

        if (cambiar !=  null) {
          cambiar.stream().forEach(p -> { id.add(p.getInteger("id"));
                                          preguntas.add(p.getString("pregunta"));
                                          /*  Aun no implementado como quiero
                                          correcta.add(p.getString("respuestaCorrecta"));
                                          mal1.add(p.getString("wrong1"));
                                          mal2.add(p.getString("wrong2"));
                                          mal3.add(p.getString("wrong3"));
                                          activa.add(p.getString("active"));*/
                                        });
        }

        try {

          session.getRemote()
                 .sendString(String.valueOf(new JSONObject().put("id", id)
                                                            .put("pregunta", preguntas)
                                                            /*  Aun no implementado como quiero
                                                            .put("correcta", correcta)
                                                            .put("mal1", mal1)
                                                            .put("mal2", mal2)
                                                            .put("mal3", mal3)
                                                            .put("activa", activa)*/
                                                            ));

        } catch (Exception r){
          r.printStackTrace();
        }

        App.closeDB();

    }

}
