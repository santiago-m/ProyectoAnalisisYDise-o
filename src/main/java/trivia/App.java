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
        		clearScreen();
        	}
        	else {
        		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");

                // De aca saque lo de recolectar datos.. porque si borrabas todos los datos, no tenia sentido pedir el usuario y contrasenia
                // Seria mas util un case.. Ver despues
        
     			if (operacion == 1) {
                    User usuario = recolectarDatos();
     			    logIn(usuario);
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

    public static int menuAdministracion() {
    	int opcion = 0;
        while ((opcion != 2) && (opcion != 1) && (opcion != 3) && (opcion != 4)) {
            clearScreen();

            System.out.println("------------------Menu Inicial---------------------");
            System.out.println();
            System.out.println("1) Crear pregunta.");
            System.out.println("2) Listar Preguntas Activas.");
            System.out.println("3) Administrar Preguntas.");
            System.out.println("4) Log out.");
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
            System.out.println("3) Log Out");
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

    		if (operacion == 4) {
    			logOut = true;
    			clearScreen();
    		}
    		else {
    			if (operacion == 1) {

    			}
    			else if (operacion == 2) {

    			}
    			else {

    			}
    		}
    	}
    }

    public static void jugar() {
    	boolean logOut = false;

    	while (!logOut) {
    		int operacion = menuJugar();

    		if (operacion == 3) {
    			logOut = true;
    			clearScreen();
    		}
    		else {
    			if (operacion == 1) {
    			}
    			else {

    			}
    		}
    	}

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
        		System.out.println("Log In Exitoso. Presione Enter para proseguir.");
        		read();

        		jugar();
        	}
        	else {
        		usuario = listAdmins.get(0);
        		System.out.println("Log In Exitoso. Presione Enter para proseguir.");
        		read();

        		administrar();
        	}
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
}

