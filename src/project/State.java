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
	public int spaces; // available spaces left

	public State(int[][] init, int spaces) {
		this.spaces = spaces;
		board = init;
	}

	public boolean move(String input, boolean player) {
		int i = input.charAt(0) - 97;
		if (i < 0)
			i = input.charAt(0) - 65;
		int j = input.charAt(1) - 49;
		return move(i, j, player);
	}

	public boolean move(int i, int j, boolean player) {
		// need to test edge cases
		if (i > board.length || j > board.length || i < 0 || j < 0
				|| board[i][j] != 0)
			return false;
		
		--spaces;
		if (player)
			board[i][j] = -1;
		else
			board[i][j] = 1;
		return true;
	}

	public boolean undo(int i, int j) {
		if (board[i][j] == 0)
			return false;
		else {
			++spaces;
			board[i][j] = 0;
			return true;
		}
	}

	/**
	 * This method literally searches through each row and column, searching for
	 * any row of 4. The method returns 1 if player 1 wins, -1 if player 2 wins
	 * or 0 if there is currently no winner.
	 */
	public int checkWin() {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				if (board[i][j] == 1 || board[i][j] == -1)
					if (checkFour(i, j, board[i][j]))
						return board[i][j];
		return 0;
	}

	private boolean checkFour(int i, int j, int type) {
		if (j + 4 <= board.length && checkI(i, j, type))
			return true;
		if (i + 4 <= board.length && checkJ(i, j, type))
			return true;
		return false;
	}

	private boolean checkI(int i, int j, int type) {
		for (int n = j; n < j + 4; ++n)
			if (board[i][n] != type)
				return false;
		return true;
	}

	private boolean checkJ(int i, int j, int type) {
		for (int n = i; n < i + 4; ++n)
			if (board[n][j] != type)
				return false;
		return true;
	}
	
	/**
	 * Prints out a given state
	 */
	public void print() {
		System.out.println("  1 2 3 4 5 6 7 8");
		char c = 'a';
		for (int i = 0; i < board.length; i++) {
			System.out.print("" + (c++) + " ");
			for (int j = 0; j < board.length; j++)
				if (board[i][j] == 0)
					System.out.print("- ");
				else if (board[i][j] == 1)
					System.out.print("X ");
				else
					System.out.print("O ");
			System.out.println("");
		}
	}
}
