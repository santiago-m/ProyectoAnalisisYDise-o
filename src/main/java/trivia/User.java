package trivia;
import java.util.Scanner;

import org.javalite.activejdbc.Model;

public class User extends Model {
	private Scanner in = new Scanner(System.in);
	private String username;
	private String password;
	private int points;

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

	public String getPassword(){
		return password;
	}

	public int getPoints() {
		return points;
	}

	public void incPoints() {
		points+= 5;
	}

	public void setPoints(int puntaje) {
		points = puntaje;
	}


  	static{
    	validatePresenceOf("username").message("Please, provide your username");
  	}
}
