package trivia;

import org.javalite.activejdbc.Model;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
  * Clase User que representa un usuario en la base de datos.
  * @author Maria, Santiago Jose; Rivero, Matias.
*/
public class User extends Model {
	private int sessionPos;
	private String username;
	private String password;
	private int points;
	private double life;

	public User() {
		super();
	}

	public User(String name, String pass) {
		super();
		username = name;
		password = pass;
		points = 0;
		life = 100;
	}

	public void setSession(int pos) {
		sessionPos = pos;
	}

	public int getSessionPos() {
		return sessionPos;
	}

	public static Map obtenerPregunta(String username) {
		App.openDB();

		List<User> users = User.where("username = '"+username+"'");
		User player = users.get(0);

		Map preguntas = new HashMap();
        preguntas.put("ID", -1);
        preguntas.put("pregunta", "");
      	preguntas.put("opcion 1", "");
      	preguntas.put("opcion 2", "");
      	preguntas.put("opcion 3", "");
      	preguntas.put("opcion 4", "");

    	String respuestasEnOrden[];
      	List<Question> questions = Question.where("active = 1 and creador != '"+player.getString("username")+"' and ('"+player.getInteger("id")+"', id) not in (SELECT * from respondidas) ");
      	Question pregunta;
      	int preguntaActual;
      	int cantPreguntas = questions.size();


      	if (!questions.isEmpty()) {
          preguntaActual = App.randInt(1, cantPreguntas);
          pregunta = questions.get(preguntaActual-1);
        
        preguntas.put("ID", pregunta.getInteger("id"));

        pregunta.calcularOpciones();
        respuestasEnOrden = new String[pregunta.getCantOpciones()];
          
        int posicionRespCorrecta = App.randInt(1, pregunta.getCantOpciones());
        
        for (int i = 1; i <= pregunta.getCantOpciones(); i++) {

          if (i > posicionRespCorrecta) {
            preguntas.put("opcion "+String.valueOf(i), pregunta.getString("wrong"+String.valueOf(i-1)));
          }
          else if (i < posicionRespCorrecta) {
            preguntas.put("opcion "+String.valueOf(i), pregunta.getString("wrong"+String.valueOf(i)));
          }
          else {
            preguntas.put("opcion "+String.valueOf(i), (String) pregunta.get("respuestaCorrecta"));
          }
        }
        preguntas.put("pregunta", pregunta.get("pregunta"));
        preguntas.put("cantPreguntasDisponibles", questions.size());
        
        Respondida preguntaRespondida = new Respondida();
        preguntaRespondida.set("usuario", player.getInteger("id"));
        preguntaRespondida.set("pregunta", pregunta.getInteger("id"));
        preguntaRespondida.saveIt();
      }
      App.closeDB();
      return (HashMap) preguntas;
	}

	public void setUsername(String newUsername) {
		username = newUsername;
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
		if (puntaje >= 0) {
			points = puntaje;
		}
	}

	public void quitarVida(int cantPreguntas) {
		life = life*((1/cantPreguntas)*0.8);
	}

	public void recoverLife() {
		life = life + ((100-life)*0.3);
	}

	public void initializeToPlay() {
		username = getString("username");
		points = getInteger("puntaje");
		life = 100;
	}

	public double getHP() {
		return life;
	}

  	static{
    	validatePresenceOf("username").message("Please, provide your username");
  	}
}
