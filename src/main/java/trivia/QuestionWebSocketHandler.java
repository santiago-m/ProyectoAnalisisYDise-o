import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

@WebSocket
public class QuestionWebSocketHandler {

    private String user;
    private Integer answer;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = Game.getJugadorActual();
        App.userUsernameMap.put(user, username);
        App.broadcastMessage(sender = "Server", msg = (username + " joined the game"));
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = App.userUsernameMap.get(user);
        App.userUsernameMap.remove(user);
        App.broadcastMessage(sender = "Server", msg = (username + " left the game"));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        App.broadcastMessage(sender = Chat.userUsernameMap.get(user), msg = message);
    }

}
