package trivia;
import org.javalite.activejdbc.Base;
import trivia.User;
import trivia.Admin;
import trivia.SqlCommands;
import java.util.Scanner;
import java.util.List;
import java.sql.*;
import java.util.Random;


public class App
{
    private static Scanner in;
    private static User usuario;

/**
	* Metodo principal desde el cual se manejan las opciones de la aplicacion.
*/
    public static void main( String[] args )
    {   
        boolean terminar = false;
        while (!terminar) {

        	int operacion = menuInicial();

        	if (operacion == 4) {
        		terminar = true;
        		clearScreen();
        	}
        	else {
        		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");

                // De aca saque lo de recolectar datos.. porque si borrabas todos los datos, no tenia sentido pedir el usuario y contrasenia
                // Seria mas util un case.. Ver despues
        
     			if (operacion == 1) {
                    usuario = recolectarDatos();
     			    logIn();
     			}
     			else if (operacion == 2) {
                    usuario = recolectarDatos();
     			    register();
     			    System.out.println("Usuario Registrado Exitosamente!. Presione ENTER para volver al menu inicial."); 
     			    read();
      			}
     			else{
					deleteData();
      			    System.out.println("Informacion borrada. Presione ENTER para volver al menu inicial.");
      			    read();
      			}
     			
     			Base.close();
     		}
        }
    }

/**
	* Metodo que representa el menu principal de la aplicacion.
*/

    public static int menuInicial() {
        int opcion = 0;
        while ((opcion != 2) && (opcion != 1) && (opcion != 3) && (opcion != 4)) {
            clearScreen();

            System.out.println("------------------Menu Inicial---------------------");
            System.out.println();
            System.out.println("1) Ya estoy registrado. Iniciar Sesion.");
            System.out.println("2) Es mi primera vez jugando. Quiero Registrarme");
            System.out.println("3) Borrar Data");
            System.out.println("4) Salir");
            System.out.println();
            System.out.println("---------------------------------------------------");
            System.out.println("Escriba el numero de opcion correspondiente: "); 
            in = new Scanner(System.in);
            opcion = in.nextInt();
        }      
        return opcion;
    }

    public static int menuAdministracion() {
    	int opcion = 0;
        while ((opcion != 2) && (opcion != 1) && (opcion != 3) && (opcion != 4) && (opcion != 5)) {
            clearScreen();

            System.out.println("------------------Menu Inicial---------------------");
            System.out.println();
            System.out.println("1) Crear pregunta.");
            System.out.println("2) Listar Preguntas Activas.");
            System.out.println("3) Administrar Preguntas.");
            System.out.println("4) Jugar.");
            System.out.println("5) Log out.");
            System.out.println();
            System.out.println("---------------------------------------------------");
            System.out.println("Escriba el numero de opcion correspondiente: "); 
            in = new Scanner(System.in);
            opcion = in.nextInt();
        }      
        return opcion;
    }

    public static int menuJugar() {
    	int opcion = 0;
        while ((opcion != 2) && (opcion != 1) && (opcion != 3)) {
            clearScreen();

            System.out.println("------------------Menu Inicial---------------------");
            System.out.println();
            System.out.println("1) Jugar.");
            System.out.println("2) Revisar Score.");
            System.out.println("3) Dejar de jugar.");
            System.out.println();
            System.out.println("---------------------------------------------------");
            System.out.println("Escriba el numero de opcion correspondiente: "); 
            in = new Scanner(System.in);
            opcion = in.nextInt();
        }      
        return opcion;
    }

    public static void administrar() {
    	boolean logOut = false;
    	
    	while (!logOut){
    		int operacion = menuAdministracion();

    		if (operacion == 5) {
    			logOut = true;
    			clearScreen();
    		}
    		else {
    			if (operacion == 1) {
    			}
    			else if (operacion == 2) {

    			}
    			else if (operacion == 3) {

    			}
    			else {
    				jugar();
    			}
    		}
    	}
    }

    public static void jugar() {
    	uncheckAllQuestions();
    	boolean logOut = false;

    	while (!logOut) {
    		int operacion = menuJugar();

    		if (operacion == 3) {
    			logOut = true;
    			clearScreen();
    		}
    		else {
    			if (operacion == 1) {
    				List<Question> preguntas = Question.where("creador != '"+usuario.getString("username")+"'");
    				int cantPreguntas = preguntas.size();

    				if (cantPreguntas > 0) {
    					comenzarAResponder(cantPreguntas, preguntas.get(0).getInteger("id"), preguntas.get(preguntas.size()-1).getInteger("id"));	
    				}
    				else {
    					System.out.println("Lo siento, todas las preguntas disponibles fueron creadas por ti. No puedo dejarte responder.");
    					read();
    				}

    				
    			}
    			else {
    				clearScreen();
    				System.out.println("Score: "+obtenerScore());
    				read();
    			}
    		}
    	}
    }

    public static void comenzarAResponder (int cantidadPreguntas, int minIndice, int maxIndice) {
    	boolean terminar = false;
    	int cantidadRespondidas = 0;
		List<Question> aux;
		Question pregunta;
		int nroPregunta;
		int posicionRespCorrecta;
		int respuestaDada;
		String[] respuestasEnOrden;
    	
    	while ((!terminar) && (cantidadRespondidas < cantidadPreguntas)) {
    		clearScreen();

    		nroPregunta = randInt(minIndice, maxIndice);
    		aux = Question.where("id = ?", nroPregunta);
    		pregunta = aux.get(0);
    		pregunta.calcularOpciones();
    		respuestasEnOrden = new String[pregunta.getCantOpciones()];


    		if ( (!pregunta.checked()) && (!(pregunta.get("creador").equals(usuario.get("username")) )) ){
    			posicionRespCorrecta = randInt(1, pregunta.getCantOpciones());
    			
    			System.out.println(pregunta.get("pregunta"));
    			System.out.println();

    			for (int i = 1; i <= pregunta.getCantOpciones(); i++) {
    				
    				if (i > posicionRespCorrecta) {
    					respuestasEnOrden[i-1] = (String) pregunta.get("wrong"+String.valueOf(i-1));
    					System.out.println(i+") "+respuestasEnOrden[i-1]);	
    				}
    				else if (i < posicionRespCorrecta) {
    					respuestasEnOrden[i-1] = (String) pregunta.get("wrong"+String.valueOf(i));
    					System.out.println(i+") "+respuestasEnOrden[i-1]);		
    				}
    				else {
    					respuestasEnOrden[i-1] = (String) pregunta.get("respuestaCorrecta");
    					System.out.println(i+") "+respuestasEnOrden[i-1]);	
    				}
    			}
    			System.out.println();
    			System.out.println("9) Terminar el juego.");

    			System.out.println();
    			System.out.println();
    			System.out.print("Ingrese la opcion deseada: ");
    			respuestaDada = readInt();
    		
    			cantidadRespondidas++;
    			pregunta.check();

    			if (respuestaDada == 9) {
    				terminar = true;
    			}
    			else {
    				if (respuestaDada == posicionRespCorrecta) {
    					System.out.println("Respuesta correcta!! +5 puntos :D");
    					usuario.incPoints();
    					usuario.set("puntaje", usuario.getPoints());
    					usuario.saveIt();
    					read();
    				}
    			}
    		}
    	}
    	uncheckAllQuestions();
    }

    public static int obtenerScore() {
    	return (int) usuario.getPoints();

    }

/**
	* Metodo que recolecta y carga los datos proporcionados por el usuario para posteriormente verificarlos.
*/

    public static User recolectarDatos() {
        clearScreen();

        System.out.print("Username: ");
        String name = read();

        System.out.print("Password: ");
        String pass = read();

        User user = new User(name, pass);

        user.set("username", name);
        user.set("password", pass);
        user.set("puntaje", user.getPoints());

        return user;
    }

/**
	* Metodo que carga los datos desde la DB del juego para que el usuario recupere su progreso anterior. Log In
*/

    public static void logIn() {
    	boolean quieroVolver = false;
    	String resp;
    	List<User> listUsers;
    	List<Admin> listAdmins;

        String userN = usuario.getUsername();
        String userP = usuario.getPassword();

        //Controlando administradores.
        listAdmins = Admin.where("username = '"+userN+"' and password = '"+userP+"'");

        //Controlando usuarios estandar.
        listUsers = User.where("username = '"+userN+"' and password = '"+userP+"'");

        while (listAdmins.isEmpty() && listUsers.isEmpty() && (!quieroVolver)) {
            System.out.println("Lo lamento, el usuario o contrasenia ingresada es incorrecta.");
            System.out.println("Presione enter para intentarlo nuevamente o escriba 'ret' para volver al menu anterior.");
            resp = read();

            if (resp.toLowerCase().equals("ret")) {
            	quieroVolver = true;
            }
            else {
            	usuario = recolectarDatos();
				
				listAdmins = Admin.where("username = '"+userN+"' and password = '"+userP+"'");
        		listUsers = User.where("username = '"+userN+"' and password = '"+userP+"'");
            }
        }
        if (!quieroVolver) {
        	if (listAdmins.isEmpty()){
        		usuario = listUsers.get(0);
        		usuario.setPoints((Integer) usuario.get("puntaje"));
        		System.out.println("Log In Exitoso. Presione Enter para proseguir.");
        		read();

        		jugar();
        	}
        	else {
        		usuario = listAdmins.get(0);
        		usuario.setPoints((Integer) usuario.get("puntaje"));
        		System.out.println("Log In Exitoso. Presione Enter para proseguir.");
        		read();

        		administrar();
        	}
        }
    }

/**
	* Metodo que verifica los datos ingresados y si no estan en el sistema los carga en la base de datos.
*/
    public static void register() {
        String userN = usuario.getUsername();

        List<User> list = User.where("username = '"+userN+"'");

        while ( (!list.isEmpty())) {
            System.out.println("Lo lamento, el nombre de usuario ingresado ya esta registrado. Intente con otro");
            usuario = recolectarDatos();
            list = User.where("username = '"+userN+"'");
        }

	    usuario.saveIt();  // ver de capturar ecepcion usuario sin nombre
    }

/**
	* Metodo que borra la informacion local y resetea el ID autoincremental.
*/

    public static void deleteData() {
    	List<User> listaCompleta = User.findAll();
		for(User u: listaCompleta) {   //<==== this line of code will initiate the actual query to DB
   			u.delete();
		}
		SqlCommands.runQuery("jdbc:mysql://localhost/trivia", "root", "root", "ALTER TABLE users AUTO_INCREMENT = 1");
	}

/**
	* Metodo que limpia la consola.
*/

    public static void clearScreen() {
    	try {
        	final String os = System.getProperty("os.name");

        	if (os.contains("Windows")) {
	            Runtime.getRuntime().exec("cls");
    	    }
        	else {
            		System.out.print("\u001b[2J");
					System.out.flush();
        	}
    	}
    	catch (final Exception e) {
      		System.out.println("Cannot clear screen. Unknown operating system");
      		System.out.println(e);
    	}
	}

	public static String read() {
		in = new Scanner(System.in);
		return in.nextLine();
	}

	public static int readInt() {
		in = new Scanner(System.in);
		return in.nextInt();
	}

/**
	* Returns a pseudo-random number between min and max, inclusive.
	* The difference between min and max can be at most
	* <code>Integer.MAX_VALUE - 1</code>.
	*
	* @param min Minimum value
	* @param max Maximum value.  Must be greater than min.
	* @return Integer between min and max, inclusive.
	* @see java.util.Random#nextInt(int)
*/
	public static int randInt(int min, int max) {

    	// NOTE: This will (intentionally) not run as written so that folks
    	// copy-pasting have to think about how to initialize their
    	// Random instance.  Initialization of the Random instance is outside
    	// the main scope of the question, but some decent options are to have
    	// a field that is initialized once and then re-used as needed or to
    	// use ThreadLocalRandom (if using at least Java 1.7).
    	Random rand = new Random();

    	// nextInt is normally exclusive of the top value,
    	// so add 1 to make it inclusive
    	int randomNum = rand.nextInt((max - min) + 1) + min;

    	return randomNum;
	}

	public static void uncheckAllQuestions() {
		List<Question> allQuestions = Question.findAll();

		for(Question q: allQuestions) {
   			q.unCheck();
		}
	}
}

