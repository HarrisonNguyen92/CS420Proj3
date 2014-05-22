/**
 * 
 */
package project;

/**
 * @author Harrison
 * 
 */
public class State {
	public int[][] board;

	public State(int[][] init) {
		board = init;
	}

	public boolean move(String input, boolean player) {
		int i = input.charAt(0) - 97;
		if (i < 0)
			i = input.charAt(0) - 65;
		int j = input.charAt(1) - 49;
		// need to test edge cases
		if (i > board.length || j > board.length || i < 0 || j < 0
				|| board[i][j] != 0)
			return false;
		if (player)
			board[i][j] = 1;
		else
			board[i][j] = -1;
		return true;
	}

	public void move(Action a) {
		if (board[a.i][a.j] == 0) {
			board[a.i][a.j] = 1;
		}
	}

	public int[][] h() {
		int[][] h = new int[8][8];

		// fill heuristic board

		return h;
	}

	/**
	 * This method literally searches through each row and column, searching for
	 * any row of 4. The method returns 1 if player 1 wins, -1 if player 2 wins
	 * or 0 if there is currently no winner.
	 */
	public int checkWin() {
		int p1 = 0;
		int p2 = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 0) {
					p1 = 0;
					p2 = 0;
				} else if (board[i][j] == 1) {
					p1++;
					p2 = 0;
				} else {
					p1 = 0;
					p2++;
				}
				if (p1 == 4)
					return 1;
				else if (p2 == 4)
					return -1;
			}
		}
		p1 = 0;
		p2 = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[j][i] == 0) {
					p1 = 0;
					p2 = 0;
				} else if (board[j][i] == 1) {
					p1++;
					p2 = 0;
				} else {
					p1 = 0;
					p2++;
				}
				if (p1 == 4)
					return 1;
				else if (p2 == 4)
					return -1;
			}
		}
		return 0;
	}
}
