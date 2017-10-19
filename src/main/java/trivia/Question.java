package trivia;

import org.javalite.activejdbc.Model;

import org.json.JSONObject;
import com.google.gson.Gson;

/**
  * Clase Question que representa una pregunta en la base de datos.
  * @author Maria, Santiago Jose; Rivero, Matias.
*/
public class Question extends Model {
	private int cantOpciones = 1;
  /*private static Gson gson = new Gson();*/

	static{
      blankToNull("wrong2", "wrong3");
    	validatePresenceOf("pregunta").message("Please, provide a question");
      validatePresenceOf("respuestaCorrecta").message("Please, provide a correct answer");
      validatePresenceOf("wrong1").message("Please, provide at least a wrong answer");
  	}

  	public boolean checked(){
  		if (!(this.getBoolean("leido"))) {
  			return false;
  		}
  		else {
  			return true;
  		}
  	}

    /*public static String toJson (Question q) {
      System.out.println(q.get(attributes));
      return gson.toJson(q.get(attributes));
    }*/

  	public void check() {
  		this.set("leido", 1);
  		this.saveIt();
  	}

  	public void unCheck(){
  		this.set("leido", 0);
  		this.saveIt();	
  	}

  	public void calcularOpciones() {
  		if (this.get("wrong1") != null) {
			cantOpciones++;
		}
		if (this.get("wrong2") != null) {
			cantOpciones++;
		}
		if (this.get("wrong3") != null) {
			cantOpciones++;
		}
  	}

  	public int getCantOpciones() {
  		return cantOpciones;
  	}
}