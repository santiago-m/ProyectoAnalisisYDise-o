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

            System.out.println("------------------Menu Administracion---------------------");
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

            System.out.println("------------------Menu Jugador---------------------");
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
                    clearScreen();
    				crearPregunta();
                    System.out.println("Operacion finalizada con exito. Presione enter para continuar.");
                    read();
    			}
    			else if (operacion == 2) {
                    clearScreen();
                    listarActivas();
                    System.out.println("No hay mas preguntas que mostrar. Presiones enter para continuar.");
                    read();
    			}
    			else if (operacion == 3) {
                    clearScreen();
                    administrarPreguntas();

    			}
    			else {
    				jugar();
    			}
    		}
    	}
    }

    private static void listarActivas() {
        List<Question> allQuestions = Question.findAll();
        Question preguntaActual;
        for (int i = 0; i < allQuestions.size(); i++) {
            preguntaActual = new Question();
            preguntaActual = allQuestions.get(i);

            if (preguntaActual.getBoolean("active")) {
                System.out.println("Pregunta "+(i+1)+":");
                System.out.println(preguntaActual.getString("pregunta"));
                System.out.println();
                System.out.println("    "+preguntaActual.getString("respuestaCorrecta"));
                System.out.println("    "+preguntaActual.getString("wrong1"));
                System.out.println("    "+preguntaActual.getString("wrong2"));
                System.out.println("    "+preguntaActual.getString("wrong3"));
                System.out.println();
                System.out.println();
            }
        }
    }

    private static void administrarPreguntas() {
        List<Question> allQuestions = Question.findAll();

        for (int i = 0; i < allQuestions.size(); i++) {
            Question preguntaActual = allQuestions.get(i);

            System.out.println("Pregunta "+(i+1)+":");
            System.out.println(preguntaActual.getString("pregunta"));
            System.out.println();
            System.out.println("    "+preguntaActual.getString("respuestaCorrecta"));
            System.out.println("    "+preguntaActual.getString("wrong1"));
            System.out.println("    "+preguntaActual.getString("wrong2"));
            System.out.println("    "+preguntaActual.getString("wrong3"));

            if (preguntaActual.getBoolean("active")) {
                System.out.println("ACTIVA");
            }
            else {
                System.out.println("NO ACTIVA");   
            }
            System.out.println();
            System.out.println();
        }

        System.out.print("Seleccione el numero correspondiente a la pregunta que desea administrar: ");
        int preg = readInt();

        Question pregunta = allQuestions.get(preg-1);

        editQuestion(pregunta);
    }

    private static void editQuestion(Question pregunta) {
        boolean terminar = false;
        String modif;

        while (!terminar) {
            int operacion = menuQuestion();

        switch (operacion) {
            case 1: System.out.println("Pregunta Actual: "+ pregunta.getString("pregunta"));
                    System.out.print("Pregunta Modificada: ");
                    modif = read();
                    pregunta.set("pregunta", modif);
                    break;

            case 2: System.out.println("Respuesta Correcta Actual: "+ pregunta.getString("respuestaCorrecta"));
                    System.out.print("Nueva Correcta: ");
                    modif = read();
                    pregunta.set("respuestaCorrecta", modif);
                    break;

            case 3: System.out.println("Opcion Incorrecta Actual: "+ pregunta.getString("wrong1"));
                    System.out.print("Nueva Incorrecta: ");
                    modif = read();
                    pregunta.set("wrong1", modif);
                    break;

            case 4: System.out.println("Opcion Incorrecta Actual: "+ pregunta.getString("wrong2"));
                    System.out.print("Nueva Incorrecta: ");
                    modif = read();
                    pregunta.set("wrong2", modif);
                    break;

            case 5: System.out.println("Opcion Incorrecta Actual: "+ pregunta.getString("wrong3"));
                    System.out.print("Nueva Incorrecta: ");
                    modif = read();
                    pregunta.set("wrong3", modif);
                    break;

            case 6: System.out.println("Estado anterior: "+(pregunta.getBoolean("active")?"Activa":"No Activa"));
                    pregunta.set("active", (!(pregunta.getBoolean("active"))));
                    System.out.println("Estado Actual: "+(pregunta.getBoolean("active")?"Activa":"No Activa"));
                    break;

            case 7: System.out.print("Desea Guardar los cambios? SI/NO: ");
                    if (read().toLowerCase().equals("si")) {
                        pregunta.saveIt();
                        System.out.println("Cambios guardados.");
                    }
                    else {
                        System.out.println("Cambios descartados.");
                    }
                    System.out.println("Presione enter para volver.");
                    read();
                    terminar = true;
                    break;
            }
        }
    }

    private static int menuQuestion() {
        int resp = 0;
        while ((resp < 1 || resp > 7)) {
            System.out.println("1) Editar Pregunta");
            System.out.println("2) Editar Respuesta Correcta");
            System.out.println("3) Editar Incorrecta 1");
            System.out.println("4) Editar Incorrecta 2");
            System.out.println("5) Editar Incorrecta 3");
            System.out.println("6) Activar/Desactivar");
            System.out.println("7) Terminar");

            System.out.println();
            resp = readInt();
        }
        return resp;
    }

    /**
        * Metodo que permite a un administrador crear una pregunta.
    */
    
    private static void crearPregunta() {
    	Question pregunta = new Question();
    	System.out.print("Pregunta: ");
        pregunta.set("pregunta", read());
        System.out.print("Respuesta Correcta: ");
        pregunta.set("respuestaCorrecta", read());

        System.out.println("Cuantas opciones incorrectas va a tener tu pregunta? (1;3)");
        int cant = readInt();

        for (int i = 0; i < cant; i++) {
            System.out.println("Respuesta incorrecta "+i+": ");
            pregunta.set("wrong"+String.valueOf(i+1), read());
        }

        System.out.println("Desea guardar la pregunta en la base de datos? YES/NO");
        if (read().toLowerCase().equals("yes")) {
            pregunta.set("creador", usuario.getString("username"));
            pregunta.set("leido", false);
            pregunta.set("active", false);

            pregunta.saveIt();
        }
    }

    private static void jugar() {
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


/**
    * Metodo que permite al usuario responder las preguntas.
*/
    private static void comenzarAResponder (int cantidadPreguntas, int minIndice, int maxIndice) {
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
    				else {
    					System.out.println("Respuesta Incorrecta.- -100 puntos :(");
    					read();
    					System.out.println("Nah, mentira. No perdiste puntos pero si respondiste mal. :D");
    					System.out.println("Presione enter para pasar a la siguiente pregunta");
    					read();
    				}
    			}
    		}
    	}
    	uncheckAllQuestions();
    }

/**
    * Metodo que devuelve el puntaje del usuario.-
*/
    private static int obtenerScore() {
    	return (int) usuario.getPoints();
    }

/**
	* Metodo que recolecta y carga los datos proporcionados por el usuario para posteriormente verificarlos.
*/

    private static User recolectarDatos() {
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

    private static void logIn() {
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
    private static void register() {
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

    private static void deleteData() {
    	List<User> listaCompleta = User.findAll();
		for(User u: listaCompleta) {   //<==== this line of code will initiate the actual query to DB
   			u.delete();
		}
		SqlCommands.runQuery("jdbc:mysql://localhost/trivia", "root", "root", "ALTER TABLE users AUTO_INCREMENT = 1");
	}

/**
	* Metodo que limpia la consola.
*/

    private static void clearScreen() {
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

	private static String read() {
		in = new Scanner(System.in);
		return in.nextLine();
	}

	private static int readInt() {
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
	private static int randInt(int min, int max) {

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


