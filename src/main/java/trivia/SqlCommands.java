package trivia;

import java.sql.*;
/**
	* Clase que permite correr un comando de sql a traves de un metodo estatico.
*/

public class SqlCommands {

/**
	* Metodo que permite a traves de un pasaje de parametros correr un comando de sql.
	* @param db link a la base de datos.
	* @param username username de la base de datos.
	* @param pass password de la base de datos.
	* @param query comando que se desea ejecutar.
*/	
	public static void runQuery(String db, String username, String pass, String query) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(db, username, pass);
      		Statement stmt = con.createStatement();	
      		stmt.execute(query);

		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Unable to restore id.");
		}
		
	}	
}