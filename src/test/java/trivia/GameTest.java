package trivia;

import trivia.Game;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class GameTest{
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/sparkTest_test", "root", "root");
        System.out.println("GameTest DB Connect");
        Base.openTransaction();


    }

    @After
    public void after(){
        System.out.println("GameTest DB Disconnec");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void prueba(){
      App.openDB();

      User usuario = new User();
      usuario.set("username", "user");
      usuario.set("password", "pass");
      usuario.saveIt();

      Game game = new Game();

      game.set("jugador1", usuario.getInteger("id"));
      game.set("jugador2", -1);
      game.set("ganador", -1);

      game.set("estado", "activo");
      game.saveIt();

      game.setPlayer1(usuario);
      game.setPlayer2(null);

      game.setCantUsuarios(1);

      App.closeDB();

      assertEquals(game.isValid(), true);
    }
}
