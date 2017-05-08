package trivia;
import org.javalite.activejdbc.Base;
import trivia.User;
import trivia.Admin;
import trivia.SqlCommands;
import java.util.Scanner;
import java.util.List;
import java.sql.*;


public class App
{
    private static Scanner in;

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
        	}
        	else {
        		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");

                // De aca saque lo de recolectar datos.. porque si borrabas todos los datos, no tenia sentido pedir el usuario y contrasenia
                // Seria mas util un case.. Ver despues
        
     			if (operacion == 1) {
                    User usuario = recolectarDatos();
     			    logIn(usuario);
     			    System.out.println("Log In Exitoso. Presione Enter para proseguir.");
        			read();
     			}
     			else if (operacion == 2) {
                    User usuario = recolectarDatos();
     			    register(usuario);
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

    public static void logIn(User usuario) {
    	boolean quieroVolver = false;
    	String resp;

        String userN = usuario.getUsername();
        String userP = usuario.getPassword();

        List<User> listUsers = User.where("username = '"+userN+"' and password = '"+userP+"'");

        while (listUsers.isEmpty() && (!quieroVolver)) {
            System.out.println("Lo lamento, el usuario o contrasenia ingresada es incorrecta.");
            System.out.println("Presione enter para intentarlo nuevamente o escriba 'ret' para volver al menu anterior.");
            resp = read();

            if (resp.toLowerCase().equals("ret")) {
            	quieroVolver = true;
            }
            else {
            	usuario = recolectarDatos();
            	listUsers = User.where("username = '"+userN+"' and password = '"+userP+"'");
            }
        }
        if (!quieroVolver) {
        	usuario = listUsers.get(0);
        }
    }

/**
	* Metodo que verifica los datos ingresados y si no estan en el sistema los carga en la base de datos.
*/
    public static void register(User usuario) {
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
            	//Runtime.getRuntime().exec("clear");
                System.out.print('\u000C');
        	}
    	}
    	catch (final Exception e) {
      		System.out.println("Cannot clear screen. Unknown operating system");
    	}
	}

	public static String read() {
		in = new Scanner(System.in);
		return in.nextLine();
	}
}

