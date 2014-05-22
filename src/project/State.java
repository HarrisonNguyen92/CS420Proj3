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

	public State() {
		board = new int[8][8];
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
}
