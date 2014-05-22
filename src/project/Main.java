/**
 * 
 */
package project;

import java.util.Scanner;

/**
 * @author Harrison
 * 
 */
public class Main {
	final static int N = 8; // I like to keep such things customizable

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String input;
		int[][] init = new int[N][N];
		// I forget whether or not java initialize all ints as 0 or null, remove
		// if unnecessary
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				init[i][j] = 0;
			}
		}

		State state = new State(init);
		printState(state);

		// not sure, but I think the board would fill up, resulting in a draw
		// after (N*N)/2 turns
		for (int i = 0; i < (N * N) / 2; i++) {
			System.out.print("Choose your next move: ");
			input = sc.nextLine();
			while (!state.move(input, true)) {
				System.out.print("Invalid input, try again: ");
				input = sc.nextLine();
			}
			printState(state);
			if (state.checkWin() != 0)
				break;
		}
		int win = state.checkWin();
		switch (win) {
		case 0:
			System.out.println("DRAW");
			break;
		case 1:
			System.out.println("PLAYER 1 WINS");
			break;
		case 2:
			System.out.println("PLAYER 2 WINS");
		}

	}

	// I = how far down the board, J = how far right

	public static Action convert(String s) {
		// a = /u97, 0 = /u48
		return new Action(s.charAt(0) - 97, s.charAt(1) - 48 - 1);
	}

	public static String convert(Action a) {
		return Character.toString((char) (a.i + 97))
				+ Character.toString((char) (a.j + 49));
	}

	/**
	 * Prints out a given state
	 */
	public static void printState(State state) {
		System.out.println("  1 2 3 4 5 6 7 8");
		char c = 'A';
		for (int i = 0; i < N; i++) {
			System.out.print("" + (c++) + " ");
			for (int j = 0; j < N; j++)
				if (state.board[i][j] == 0)
					System.out.print("- ");
				else if (state.board[i][j] == 1)
					System.out.print("X ");
				else
					System.out.print("O ");
			System.out.println("");
		}
	}
}
