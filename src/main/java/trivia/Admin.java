package trivia;

/**
	* Clase Admin, extiende de User para poder logearse y por tanto tambien de Model.
*/

public class Admin extends User {
  static{
    blankToNull("username", "password");
    validatePresenceOf("username").message("Please, provide your username");
    validatePresenceOf("password").message("Please, provide a password");
  }
}
