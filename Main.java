package pacman;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	// Recorre el array bidimensional y va mostrando su contenido

	public static void mostrarTablero(String[][] tablero) {
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				System.out.print(tablero[i][j]);
			}
			System.out.println("");
		}
	}

	/*
	 * Primero genera el pacman, despues crea los fantasmas cerciorandose q no
	 * sobreescriben ningun otra pieza
	 */
	public static void generarPiezas(String[][] tablero, boolean[][]tablero_bool) {
		int i = (int) (Math.random() * tablero.length);
		int j = (int) (Math.random() * tablero.length);

		tablero[i][j] = "C ";
		tablero_bool[i][j]=true;

		int cont = 0;

		do {
			int i2 = (int) (Math.random() * tablero.length);
			int j2 = (int) (Math.random() * tablero.length);
		

			if (tablero_bool[i2][j2]==false) {
				if (cont == 0) {
					tablero[i2][j2] = "X ";
				} else if (cont == 1) {
					tablero[i2][j2] = "Y ";
				} else {
					tablero[i2][j2] = "Z ";
				}

				cont++;
				tablero_bool[i2][j2]=true;
			}
		} while (cont < 3);

	}

	// le pasas la pieza que quieres q busque en el tablero y te devuelve un array
	// con sus coordenadas

	public static Integer[] buscarPieza(String[][] tablero, String pattern) {
		Integer[] pos = new Integer[2];
		String pieza = "";

		if (pattern.equals("C ")) {
			pieza = "C ";
		} else if (pattern.equals("X ")) {
			pieza = "X ";
		} else if (pattern.equals("Y ")) {
			pieza = "Y ";
		} else if (pattern.equals("Z ")) {
			pieza = "Z ";
		}

		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				if (tablero[i][j].equals(pieza)) {
					pos[0] = i;
					pos[1] = j;
				}
			}
		}

		return pos;
	}

	// recorre el tablero buscando "· " y las cuenta

	public static int contarManzanas(String tablero[][]) {
		int cont = 0;

		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				if (tablero[i][j].equals("· ")) {
					cont++;
				}
			}
		}
		return cont;
	}

	// le paso el tablero y la posicon para wasd resto o sumo en la coordenada
	// pertinente
	// la posicion previa la pongo como " "
	public static void moverPacMan(String tablero[][], Integer[] posPacMan) {
		String movement = "";
		int i_previo = posPacMan[0];
		int j_previo = posPacMan[1];

		do {
			movement = new Scanner(System.in).nextLine();

			if (movement.equals("w")) {
				posPacMan[0]--;
				if (posPacMan[0] < 0) {
					posPacMan[0] = 0;
				}
			} else if (movement.equals("a")) {
				posPacMan[1]--;
				if (posPacMan[1] < 0) {
					posPacMan[1] = 0;
				}
			} else if (movement.equals("s")) {
				posPacMan[0]++;
				if (posPacMan[0] > tablero.length-1) {
					posPacMan[0] = tablero.length-1;
				}
			} else if (movement.equals("d")) {
				posPacMan[1]++;
				if (posPacMan[1] > tablero.length-1) {
					posPacMan[1] = tablero.length-1;
				}
			}

		} while (!(movement.equals("w") || movement.equals("a") || movement.equals("s") || movement.equals("d")));

		tablero[i_previo][j_previo] = "  ";
		tablero[posPacMan[0]][posPacMan[1]] = "C ";
	}
	
	
	//le pasas una posicion y de devuelve true si pacman ya ha pasado previamente por esa posicio. False si no.
	public static boolean historalPacman(List<Integer[]> historal, Integer[] pos) {
		boolean check = false;

		for (int i = 0; i < historal.size(); i++) {
			Integer[] temp = historal.get(i);
			if (temp[0].equals(pos[0]) && temp[1].equals(pos[1])) {
				check = true;
				break;
			}
		}

		return check;
	}

	// igual que con el pacman pero haciendolo aleatorio y teniendo en cuenta si dejar una manzada o dejar en blanco la posicion previa
	public static void moverFantasma(String tablero[][], Integer[] posF1, String pattern, List<Integer[]> historal) {

		// con esta var eligire la coordenada que muevo
		int choose_coord = (int) (Math.random() * 2);
		// con esta eligire si sumo o resto a la coordenada
		int suma_resta = (int) (Math.random() * 2);

		if (suma_resta == 0) {
			suma_resta = -1;
		} else {
			suma_resta = 1;
		}
		
		//guardo la posicion actual en un array de integer que usare para chequear si pacman ya ha pasado por aqui
		int i_previo = posF1[0];
		int j_previo = posF1[1];
		Integer[] pos_previa = new Integer[2];
		pos_previa[0]= i_previo;
		pos_previa[1]=j_previo;

		if (choose_coord == 0) {
			posF1[0] += suma_resta;

			if (posF1[0] < 0) {
				posF1[0] = 0;
			} else if (posF1[0] > tablero.length-1) {
				posF1[0] = tablero.length-1;
			}
		} else {
			posF1[1] += suma_resta;

			if (posF1[1] < 0) {
				posF1[1] = 0;
			} else if (posF1[1] > tablero.length-1) {
				posF1[1] = tablero.length-1;
			}
		}
		
		//chequeo si la posicion de la que viene está en el recorrido del pacman para saber si poner una manzana o no
		boolean check = historalPacman(historal, pos_previa);

		if (check) {
			//si se va a chocar con otro fantasma, no se mueve
			if(!(tablero[posF1[0]][posF1[1]].equals("X ") || tablero[posF1[0]][posF1[1]].equals("Y ") || tablero[posF1[0]][posF1[1]].equals("Z "))) {
				tablero[i_previo][j_previo] = "  ";
				tablero[posF1[0]][posF1[1]] = pattern;
			}else {
				tablero[i_previo][j_previo] =pattern;
			}
			
		} else {
			if(!(tablero[posF1[0]][posF1[1]].equals("X ") || tablero[posF1[0]][posF1[1]].equals("Y ") || tablero[posF1[0]][posF1[1]].equals("Z "))) {
				tablero[i_previo][j_previo] = "· ";
				tablero[posF1[0]][posF1[1]] = pattern;
			}else {
				tablero[i_previo][j_previo] =pattern;
			}
		}
	}
	//busco "C " en el tablero, si no está, significa que ha muerto
	public static boolean checkMuerte(String tablero[][]) {
		boolean check = true;
		
		String pieza = "C ";
		
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				if (tablero[i][j].equals(pieza)) {
					check=false;
				}
			}
		}
		
		return check;
	}

	public static void main(String[] args) {
		
		int x = 20;
		int y = 20;
		boolean check=false;

		String[][] tablero = new String[x][y];
		//este tablero lo voy a usar para que las piezas no se puedan generar una encima de otra
		boolean[][]tablero_bool = new boolean[x][y];

		// en esta lista voy a guardar un historial de por donde ha pasado pacman para
		// saber como tengo que mover correctamente los fantasmas
		List<Integer[]> historalPacman = new ArrayList<Integer[]>();

		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				tablero[i][j] = "· ";
			}
		}
		//lleno todo el tablero de bool con false
		for (int i = 0; i < tablero_bool.length; i++) {
			for (int j = 0; j < tablero_bool.length; j++) {
				tablero_bool[i][j] = false;;
			}
		}

		generarPiezas(tablero, tablero_bool);
		mostrarTablero(tablero);

		int numManzanas = contarManzanas(tablero);

		do {
			Integer[] posPacMan = buscarPieza(tablero, "C ");
			Integer[] posF1 = buscarPieza(tablero, "X ");
			Integer[] posF2 = buscarPieza(tablero, "Y ");
			Integer[] posF3 = buscarPieza(tablero, "Z ");

			historalPacman.add(new Integer[]{posPacMan[0], posPacMan[1]});

			moverPacMan(tablero, posPacMan);
			moverFantasma(tablero, posF1, "X ", historalPacman);
			moverFantasma(tablero, posF2, "Y ", historalPacman);
			moverFantasma(tablero, posF3, "Z ", historalPacman);
			

			check=checkMuerte(tablero);
			
			mostrarTablero(tablero);
			numManzanas = contarManzanas(tablero);
		} while (numManzanas > 0 && !check);
		
		if(numManzanas==0) {
			System.out.println("YOU WIN");
		}else {
			System.out.println("YOU LOSE");
		}

	}

}
