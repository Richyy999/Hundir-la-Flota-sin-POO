package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

/**
 * Hundir la flota sin orientación a objetos. Cada barco es un HashMap con una
 * clave que son las coordenadas de una parte del barco y el valor es el estado
 * del barco entero o tocado. Y cada vez que se hunde un barco se añade a un
 * HashSet de barcos hundidos. La partida acaba cuando se hunden todos los
 * barcos o se alcanza el límite de turnos de los que dispone el atacante para
 * hundir la flota del que defiende. P.D: Sin los comentarios el código son 260
 * líneas
 * 
 * @author Ricardo Bordería Pi
 *
 */
public class MainHudirLaFlota {

	private static Scanner sc;

	/**
	 * El portaaviones
	 */
	private static Map<String, String> p;

	/**
	 * El submarino 1
	 */
	private static Map<String, String> s1;
	/**
	 * El submarino 2
	 */
	private static Map<String, String> s2;

	/**
	 * La fragata 1
	 */
	private static Map<String, String> f1;
	/**
	 * La fragata 2
	 */
	private static Map<String, String> f2;

	/**
	 * La lancha 1
	 */
	private static Map<String, String> l1;
	/**
	 * La lancha 2
	 */
	private static Map<String, String> l2;

	/**
	 * HashSet de los barcos hundidos
	 */
	private static Set<Map<String, String>> barcosHundidos;

	/**
	 * Tablero del que dispone el atacante para realizar los disparos
	 */
	private static char[][] tablero;
	/**
	 * Tablero del que dispone el defensor para ayudarle a ubicar la flota
	 */
	private static char[][] tableroCreacion;

	public static void main(String[] args) {
		sc = new Scanner(System.in);
		tablero = new char[10][10];
		tableroCreacion = new char[10][10];
		cargarTablero(tablero);
		cargarTablero(tableroCreacion);
		barcosHundidos = new HashSet<Map<String, String>>();

		p = new HashMap<String, String>();

		s1 = new HashMap<String, String>();
		s2 = new HashMap<String, String>();

		f1 = new HashMap<String, String>();
		f2 = new HashMap<String, String>();

		l1 = new HashMap<String, String>();
		l2 = new HashMap<String, String>();

		crearBarcos(p, 5);
		mostrarTablero(tableroCreacion);

		crearBarcos(s1, 3);
		mostrarTablero(tableroCreacion);
		crearBarcos(s2, 3);
		mostrarTablero(tableroCreacion);

		crearBarcos(f1, 3);
		mostrarTablero(tableroCreacion);
		crearBarcos(f2, 3);
		mostrarTablero(tableroCreacion);

		crearBarcos(l1, 2);
		mostrarTablero(tableroCreacion);
		crearBarcos(l2, 2);
		mostrarTablero(tableroCreacion);

		esconderTablero();

		/**
		 * Turno actual
		 */
		int turno = 0;
		/**
		 * Límite de turnos para ganar la partida
		 */
		int turnoMax = 60;
		while (barcosHundidos.size() < 7 && turno < turnoMax) {
			disparar();
			mostrarTablero(tablero);
			turno++;
		}
		if (turno < turnoMax)
			System.out.println("Victoria");
		else
			System.out.println("Derrota. Se acabó el límite de tiempo.");
		sc.close();
	}

	/**
	 * Esconde el tableroCreacion para que el atacante no lo vea
	 */
	private static void esconderTablero() {
		for (int i = 0; i < 20; i++) {
			System.out.println("");
		}
	}

	/**
	 * Imprimo el tablero
	 * 
	 * @param tablero tablero a imprimir
	 */
	private static void mostrarTablero(char[][] tablero) {
		System.out.println("  0 1 2 3 4 5 6 7 8 9 X");
		for (int i = 0; i < tablero.length; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < tablero.length; j++) {
				System.out.print(tablero[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println("Y");
	}

	/**
	 * Carga el tablero en su aspecto inicial
	 * 
	 * @param tablero tablero para inicializar
	 */
	private static void cargarTablero(char[][] tablero) {
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				tablero[i][j] = '*';
			}
		}
	}

	/**
	 * Realizo y proceso el disparo
	 */
	private static void disparar() {
		// Pido las coordenadas del ataque
		System.out.println("Introduce la coordenada x [0,9] del disparo: ");
		int x = Integer.parseInt(sc.nextLine());
		System.out.println("Introduce la coordenada y [0,9] del disparo: ");
		int y = Integer.parseInt(sc.nextLine());

		// Compruebo si las coordenadas están dentro del tablero
		if (x < 0 || y < 0 || x > 9 || y > 9)
			System.out.println("Las coordenadas del ataque se salen del mapa");
		// Compruebo si las coordenadas codificadas pertenecen a un barco
		else {
			String clave = x + " " + y;
			if (p.containsKey(clave))
				comprobarBarco(clave, p);
			else if (s1.containsKey(clave))
				comprobarBarco(clave, s1);
			else if (s2.containsKey(clave))
				comprobarBarco(clave, s2);
			else if (f1.containsKey(clave))
				comprobarBarco(clave, f1);
			else if (f2.containsKey(clave))
				comprobarBarco(clave, f2);
			else if (l1.containsKey(clave))
				comprobarBarco(clave, l1);
			else if (l2.containsKey(clave))
				comprobarBarco(clave, l2);
			// Si no estan contenidas imprime Agua y añade una A a las coordenadas del
			// tablero
			else {
				System.out.println("Agua");
				tablero[y][x] = 'A';
			}
		}
	}

	/**
	 * Proceso el disparo sobre un barco
	 * 
	 * @param clave coordenadas codificadas de barco
	 * @param barco barco bajo el ataque
	 */
	private static void comprobarBarco(String clave, Map<String, String> barco) {
		// Compruebo si el barco ya está hundido
		if (barcosHundidos.contains(barco))
			System.out.println("El barco ya está hundido");
		// Si el barco en esas coordenadas ha sido tocado indico agua
		else if (barco.get(clave).equals("tocado"))
			System.out.println("Agua");
		// Si no actualizo el estado del barco en esas coordenadas y revelo la posición
		// del barco tocado
		else {
			barco.put(clave, "tocado");
			System.out.println("Tocado");
			// Decodifico las coordenadas para pintar el tablero
			String coor[] = clave.split(" ");
			int x = Integer.parseInt(coor[0]);
			int y = Integer.parseInt(coor[1]);
			tablero[y][x] = 'X';
		}
		// Compruebo si el barco ha sido hundido en ese ataque
		// Supongo que el barco está hundido ya que si alguna coordenada no está ticado
		// el barco no se ha hundido
		boolean hundido = true;
		for (Entry<String, String> bar : barco.entrySet()) {
			if (!bar.getValue().equals("tocado"))
				hundido = false;
		}
		// Si se ha hundido el barco informo al atacante y lo añado al HashSet de barcos
		// hundidos
		if (hundido) {
			System.out.println("Barco hundido");
			barcosHundidos.add(barco);
		}
	}

	/**
	 * Crea el barco y si está correctamente ubicado lo añade al tableroCreacion
	 * 
	 * @param barco barco a desplegar
	 * @param len   número de casillas que ocupa el barco
	 */
	private static void crearBarcos(Map<String, String> barco, int len) {
		// intuyo que el barco será creado erróneamente
		boolean incorrecto = true;
		do {
			// Pido las coordenadas iniciales y la dirección en la que desplegará el barco
			System.out.println("Introduce la coordenada x inicial [0, 9]: ");
			int x0 = Integer.parseInt(sc.nextLine());
			System.out.println("Introduce la coordenada y inicial [0, 9]: ");
			int y0 = Integer.parseInt(sc.nextLine());
			System.out.println("Introduce la dirección: ");
			String dir = sc.nextLine();

			// Creo los arrays de las coordenadas x e y respectivamente
			int x[] = new int[len];
			int y[] = new int[len];

			// Mensaje informativo que le saldrá al creador para tener feedback sobre el
			// despliegue de su flota
			String mensaje = "Barco añadido";
			// Si la creación se realiza correctamente sigo al siguiente paso
			boolean seguir = true;
			// añado las coordenadas a los arrays en función de la dirección indicada para
			// posteriormente crear el barco
			for (int i = 0; i < x.length && seguir; i++) {
				switch (dir.toLowerCase()) {
				case "arriba":
					y[i] = y0--;
					x[i] = x0;
					break;
				case "abajo":
					y[i] = y0++;
					x[i] = x0;
					break;
				case "derecha":
					y[i] = y0;
					x[i] = x0++;
					break;
				case "izquierda":
					y[i] = y0;
					x[i] = x0--;
					break;
				case "arriba izquierda":
					y[i] = y0--;
					x[i] = x0--;
					break;
				case "arriba derecha":
					y[i] = y0--;
					x[i] = x0++;
					break;
				case "abajo izquierda":
					y[i] = y0++;
					x[i] = x0--;
					break;
				case "abajo derecha":
					y[i] = y0++;
					x[i] = x0++;
					break;
				default:
					mensaje = "Disposición incorrecta";
					break;
				}
				// Compruebo si el barco cabe dentro del mapa
				// En caso contrario reinicio los arrays con las coordenadas y vuelvo a pedir
				// los datos al jugador
				if (x0 < 0 || y0 < 0 || x0 > 9 || y0 > 9) {
					x = new int[x.length];
					y = new int[y.length];
					seguir = false;
					mensaje = "El barco se sale del mapa. Cambia las coordenadas iniciales o la direción del barco";
				}
			}
			// Si al barco cabe en el mapa compruebo que no se superponga con otros barcos
			if (seguir) {
				// Si las coordenadas están contenidas en otro barco
				boolean contiene = false;
				for (int i = 0; i < y.length && !contiene; i++) {
					String clave = x[i] + " " + y[i];
					if (p.containsKey(clave))
						contiene = true;
					else if (s1.containsKey(clave))
						contiene = true;
					else if (s2.containsKey(clave))
						contiene = true;
					else if (f1.containsKey(clave))
						contiene = true;
					else if (f2.containsKey(clave))
						contiene = true;
					else if (l1.containsKey(clave))
						contiene = true;
					else if (l2.containsKey(clave))
						contiene = true;
					// Si no las contiene ningún barco las añado al barco a desplegar
					else {
						barco.put(clave, "entero");
						tableroCreacion[y[i]][x[i]] = 'X';
					}
				}
				// Si algún barco contiene alguna coordenada cambio el mensaje, reinicio el
				// barco y elimino las coordenadas en el tableroCreacion
				if (contiene) {
					mensaje = "Hay barcos superpuestos.";
					eliminarBarco(tableroCreacion, x, y);
					barco = new HashMap<String, String>();
					// Si todo es correcto actualizo incorrecto para salir del bucle
				} else
					incorrecto = false;
			}
			System.out.println(mensaje);
		} while (incorrecto);
	}

	/**
	 * elimino del tablero las coordenadas de un barco mal colocado
	 * 
	 * @param tablero tablero para eliminar el barco
	 * @param x       coordenadas x del barco a eliminar
	 * @param y       coordenadas y del barco a eliminar
	 */
	private static void eliminarBarco(char[][] tablero, int[] x, int[] y) {
		for (int i = 0; i < y.length; i++) {
			tablero[y[i]][x[i]] = 'X';
		}
	}
}
