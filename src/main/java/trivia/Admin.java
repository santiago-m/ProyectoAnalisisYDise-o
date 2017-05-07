package trivia;

import org.javalite.activejdbc.Model;

public class Admin extends Model {
  static{
    validatePresenceOf("username").message("Please, provide your username");
  }
}
