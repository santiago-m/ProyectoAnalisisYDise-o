package trivia;
import java.util.Scanner;

import org.javalite.activejdbc.Model;

public class User extends Model {
	Scanner in = new Scanner(System.in);
	String username;
	String password;
	int points;

	public User() {
		super();
	}


	public User(String name, String pass) {
		username = name;
		password = pass;
		points = 0;
	}

	public String getUsername(){
		return username;
	}

	public String getPassword(){		// public why?
		return password;
	}

	public int getPoints() {
		return points;
	}

	public void incPoint() {
		points++;
	}


  	static{
    	validatePresenceOf("username").message("Please, provide your username");
  	}
}
