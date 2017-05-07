package trivia;
import java.util.Scanner;

import org.javalite.activejdbc.Model;

public class User extends Model {
	Scanner in = new Scanner(System.in);
	String username;
	String password;
	int puntos;

	public User() {
		super();
	}


	public User(String name, String pass) {
		username = name;
		password = pass;
		puntos = 0;
	}

	public String getUsername(){
		return username;
	}

	public String getPassword(){
		return password;
	}


  	static{
    	validatePresenceOf("username").message("Please, provide your username");
  	}
}
