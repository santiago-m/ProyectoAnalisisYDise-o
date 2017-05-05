package trivia;
import org.javalite.activejdbc.Base;
import trivia.User;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
      Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");

      User u = new User();
      u.set("username", "Maradona");
      u.set("password", "messi");
      u.saveIt();

      User prueba1 = new User();
      prueba1.set("username", "gregory");
      prueba1.set("password", "asd");
      prueba1.saveIt();

      Base.close();
    }
}
