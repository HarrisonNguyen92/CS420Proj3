/**
 * 
 */
package project;

import java.util.Scanner;

/**
 * Player is represented by 1, Program is represented by -1
 * 
 * @author Harrison
 * 
 */
public class Main {
	final static int N = 8; // I like to keep such things customizable
	private static Scanner sc = new Scanner(System.in);

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		String input;
		;
		AB ai;
		Action a;
		int[][] init = new int[N][N];
		boolean player;

		State state = new State(init, N * N);

		System.out.println("How long should the program think to make a move?");
		ai = new AB(sc.nextInt());
		sc.nextLine();

		System.out.println("Are you going first? (Y/N)");
		if (sc.nextLine().toUpperCase().charAt(0) != 'Y') {
			player = false;
			printState(state);
			// program move some ab algorithm (usually the center 4 spaces)
			a = ai.absearch(state);
			state.move(a.i, a.j, player);
			a.print();
		}
		printState(state);
		player = true;
		// not sure, but I think the board would fill up, resulting in a draw
		// after (N*N)/2 turns

		while (state.spaces > 0) {
			if (player) {
				// player move
				System.out.print("Choose your next move: ");
				input = sc.nextLine();
				while (!state.move(input, player)) {
					System.out.print("Invalid input, try again: ");
					input = sc.nextLine();
				}
			} else {
				// program move
				a = ai.absearch(state);
				state.move(a.i, a.j, player);
				a.print();
			}
			if (player)
				player = false;
			else
				player = true;
			printState(state);
			if (state.checkWin() != 0)
				break;
		}
		switch (state.checkWin()) {
		case 0:
			System.out.println("DRAW");
			break;
		case 1:
			System.out.println("PLAYER 1 WINS");
			break;
		case -1:
			System.out.println("PLAYER 2 WINS");
		}

	}

	/*
	 * I = how far down the board, J = how far right
	 * 
	 * public static Action convert(String s) { // a = /u97, 0 = /u48 return new
	 * Action(s.charAt(0) - 97, s.charAt(1) - 48 - 1); }
	 * 
	 * public static String convert(Action a) { return Character.toString((char)
	 * (a.i + 97)) + Character.toString((char) (a.j + 49)); }
	 */

	/**
	 * Prints out a given state
	 */
	public static void printState(State state) {
		System.out.println("  1 2 3 4 5 6 7 8");
		char c = 'a';
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
