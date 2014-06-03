/**
 * 
 */
package project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Harrison
 * 
 */
public class AlphaBeta {
	/**
	 * The current worst value
	 */
	private int alpha;

	/**
	 * The current best value
	 */
	private int beta;

	/**
	 * the Start time of the algorithm
	 */
	private long startTime;

	/**
	 * how long the algorithm should run
	 */
	private long limit;

	private boolean first;

	private static final boolean NATHAN = true;

	public AlphaBeta(int lim, boolean b) {
		this.limit = (long) (lim * 1000);
		this.first = b;
	}

	public Action absearch(State state) {
		int score;
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		int best = alpha;
		int mi= 0, mj=0;
		startTime = System.currentTimeMillis();
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board.length; j++) {
				if (state.board[i][j] == 0) {
					state.move(i, j, false);
					score = minValue(state);
					System.out.printf("%+5d", score);
					if (score > best) {
						mi = i;
						mj = j;
						best = score;
					}
					state.undo(i, j); // undo move
				} else System.out.print("  () ");		
			}
			System.out.println();
		}
		System.out.println(best);
		return new Action(mi, mj);
	}
	

	private int maxValue(State state) {
		if (cutoff() || state.spaces <= 0 || state.checkWin() != 0)
			return eval(state);
		int best = alpha;
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board.length; j++) {
				if (state.board[i][j] == 0) {
					state.move(i, j, true);
					best = Integer.max(best, minValue(state));
					state.undo(i, j);
					if (best > beta)
						return beta;
				}
			}
		}
		return best;
	}

	private int minValue(State state) {
		if (cutoff() ||state.spaces <= 0 || state.checkWin() != 0)
			return eval(state);
		int best = beta;
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board.length; j++) {
				if (state.board[i][j] == 0) {
					state.move(i, j, false);
					best = Integer.min(best, maxValue(state));
					state.undo(i, j);
					if (best < alpha)
						return alpha;
				}
			}
		}
		return best;
	}

	/**
	 * Program will return the best solution found so far given a specific
	 * period of time
	 * 
	 * @return
	 */
	private boolean cutoff() {
		if (System.currentTimeMillis() - startTime > limit)
			return true;
		return false;
	}

	/**
	 * Calculates the score of a board state
	 * 
	 * @param s
	 * @return
	 */
	private int eval(State s) {
		if (s.checkWin() == 1)
			return Integer.MAX_VALUE / 2;
		if (s.checkWin() == -1)
			return Integer.MIN_VALUE / 2;
		if (s.spaces == 0)
			return 0;
		int score = 0;
		int x = 0;
		int o = 0;

		if (NATHAN) {
			for (int i = 0; i < s.board.length; i++)
				for (int j = 0; j < s.board.length; j++)
					score += eval(s, i, j);
			return score;
		}

		for (int i = 0; i < s.board.length; ++i)
			for (int j = 0; j < s.board.length - 3; ++j) {
				for (int k = 0; k < 4; ++k) {
					if (s.board[i][j + k] == 1)
						x++;
					if (s.board[i][j + k] == -1)
						o++;
				}
				score += x * x * 4;
				score -= o * o * 4;
				if (o == 1 && o < x)
					score -= x * x * 4;
				if (x == 1 && x < o)
					score += o * o * 4;
				x = 0;
				o = 0;

				for (int k = 0; k < 4; ++k) {
					if (s.board[j + k][i] == 1)
						x++;
					if (s.board[j + k][i] == -1)
						o++;
				}
				score += x;
				score -= o;
				if (o == 1 && x == 2)
					score -= x + 10;
				if (x == 1 && o == 2)
					score += o + 10;
				if (o == 1 && x == 3)
					score -= x + 20;
				if (x == 1 && o == 3)
					score += o + 20;
				x = 0;
				o = 0;
			}
		return score;
	}

	/**
	 * Checks 3 spaces in each direction from a space to count for potential
	 */
	private int eval(State s, int i, int j) {
		int score = 0;
		int base = 5;
		int temp = 0;
		int check = s.board[i][j];
		if (!first && check < 0)
			base += 3;

		if (i >= 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i - c][j] == check)
					temp += base - c;
				else if (s.board[i - c][j] != 0) {// path blocked
					temp = 0;
					c = 10;
				}
			if (i < s.board.length - 1)
				if (temp == 7 && s.board[i + 1][j] == 0)
					return 10000 * check;
			score += temp;
			temp = 0;
		} else
			score--;

		if (i < s.board.length - 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i + c][j] == check)
					temp += base - c;
				else if (s.board[i + c][j] != 0) {// path blocked
					temp = 0;
					c = 10;
				}
			if (i > 0)
				if (temp == 7 && s.board[i - 1][j] == 0)
					return 10000 * check;
			score += temp;
			temp = 0;
		} else
			score--;

		if (j >= 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i][j - c] == check)
					temp += base - c;
				else if (s.board[i][j - c] != 0) {// path blocked
					temp = 0;
					c = 10;
				}
			if (j < s.board.length - 1)
				if (temp == 7 && s.board[i][j + 1] == 0)
					return 10000 * check;
			score += temp;
			temp = 0;
		} else
			score--;

		if (j < s.board.length - 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i][j + c] == check)
					temp += base - c;
				else if (s.board[i][j + c] != 0) {// path blocked
					temp = 0;
					c = 10;
				}
			if (j > 0)
				if (temp == 7 && s.board[i][j - 1] == 0)
					return 10000 * check;
			score += temp;
			temp = 0;
		} else
			score--;
		return score * check;// negates if opponent
	}

}