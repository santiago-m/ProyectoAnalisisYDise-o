package trivia;
import org.javalite.activejdbc.Base;
import trivia.User;
import java.util.Scanner;
import java.util.List;

public class App
{
    static Scanner in;

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

      			User usuario = recolectarDatos();
        
     			if (operacion == 1) {
     			   logIn(usuario);
     			}
     			else if (operacion == 2) {
     			    register(usuario);
     			    System.out.println("Usuario Registrado Exitosamente."); 
     			    read();
      			}
     			else{
					List<User> listaCompleta = User.findAll();
					for(User u: listaCompleta) {   //<==== this line of code will initiate the actual query to DB
   						u.delete();
					}
      			    System.out.println("Informacion borrada. Pesione ENTER para volver al menu inicial.");
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

        user.set("username", user.username);
        user.set("password", user.password);
        user.set("puntaje", user.puntos);

        return user;
    }

/**
	* Metodo que carga los datos desde la DB del juego para que el usuario recupere su progreso anterior. Log In
*/

    public static void logIn(User usuario) {
    	boolean quieroVolver = false;
    	String resp;
        List<User> listUsers = User.where("username = '"+usuario.get("username")+"' and password = '"+usuario.get("password")+"'");

        while (listUsers.isEmpty() && (!quieroVolver)) {
            System.out.println("Lo lamento, el usuario o contraseña ingresada es incorrecta.");
            System.out.println("Presione enter para intentarlo nuevamente o escriba 'get me back' para volver al menu anterior.");
            resp = read();

            if (resp.toLowerCase() == "get me back") {
            	quieroVolver = true;
            }
            else {
            	usuario = recolectarDatos();
            	listUsers = User.where("username = '"+usuario.get("username")+"' and password = '"+usuario.get("password")+"'");
            }
        }
        usuario = listUsers.get(0);
    }

/**
	* Metodo que verifica los datos ingresados y si no estan en el sistema los carga en la base de datos.
*/
    public static void register(User usuario) {
        List<User> list = User.where("username = '"+usuario.get("username")+"'");

        while ( (!list.isEmpty())) {
            System.out.println("Lo lamento, el nombre de usuario ingresado ya esta registrado. Intente con otro");
            usuario = recolectarDatos();
            list = User.where("username = '"+usuario.get("username")+"'");
        }
	    usuario.saveIt();
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
            	Runtime.getRuntime().exec("clear");
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

