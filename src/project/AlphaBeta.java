/**
 *
 */
package project;

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
	private final long limit;

	/**
	 * depth of the algorithm (to be used with IDDFS)
	 */
	private final int depth;

	private boolean first;

	private static final boolean NATHAN = false;

	public AlphaBeta(int lim, boolean b) {
		this.limit = (long) (lim * 1000)*100;
		this.depth = lim*100;
		this.first = b;
	}

	public Action absearch(State state) {
		return absearch(state, depth);
	}

	public Action absearch(State state, int d) {
		int score;
		int mi = 0;
		int mj = 0;
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		int best = alpha;

		startTime = System.currentTimeMillis();
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board.length; j++) {
				if (state.board[i][j] == 0) {
					state.move(i, j, false);
					score = minValue(state, d - 1);
					state.undo(i, j); // undo move
					System.out.printf("%+4d", score);
					if (score > best) {
						mi = i;
						mj = j;
						best = score;
					}
				} else
					System.out.print(" [] ");
				// Potential random choice area. need list and PRNG
			}
			System.out.println();

		}
		System.out.println(best);
		return new Action(mi, mj);
	}

	private int maxValue(State state, int d) {
		if (state.checkWin() == 1)
			return Integer.MAX_VALUE / 2;
		if (state.checkWin() == -1)
			return Integer.MIN_VALUE / 2;
		if (state.spaces == 0)
			return 0;
		if (cutoff() || d <= 0)
			return eval(state);
		
		int best = alpha;
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board.length; j++) {
				if (state.board[i][j] == 0) {
					state.move(i, j, true);
					best = Integer.max(best, minValue(state, d - 1));
					state.undo(i, j); // undo move
//					if (best >= beta)
//						return best;
//					alpha = Integer.max(alpha, best);
				}
			}
		}
		return best;
	}

	private int minValue(State state, int d) {
		if (state.checkWin() == 1)
			return Integer.MAX_VALUE / 2;
		if (state.checkWin() == -1)
			return Integer.MIN_VALUE / 2;
		if (state.spaces == 0)
			return 0;
		if (cutoff() || d <= 0)
			return eval(state);
		
		int best = beta;
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board.length; j++) {
				if (state.board[i][j] == 0) {
					state.move(i, j, false);
					best = Integer.min(best, maxValue(state, d - 1));
					state.undo(i, j);
//					if (best <= alpha)
//						return best;
//					beta = Integer.min(beta, best);
				}
			}
		}
		return best;
	}

	/**
	 * Program will return the best solution found so far given a specific
	 * period of time. Return True if time limit is done
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
				score += x * x;
				score -= o * o;
				x = 0;
				o = 0;
				for (int k = 0; k < 4; ++k) {
					if (s.board[j + k][i] == 1)
						x++;
					if (s.board[j + k][i] == -1)
						o++;
				}
				score += x * x;
				score -= o * o;
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
		int temp = 0;
		int check = s.board[i][j];

		// Acts as if the move is actually an O, and checks for an O win.
		// Necessary to combat O-OO and such
		if (!first && check > 0) {
			check = -1;
			s.board[i][j] = -1;
			if (s.checkWin() == -1) {
				score += 10000;
			}
			s.board[i][j] = 1;
		}

		// Tricks AI into thinking it's Os when not first, turning him into a
		// blocker from hell
		if (!first)
			check = -1;
		if (i >= 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i - c][j] == check)
					temp += 5 - c;
				else if (s.board[i - c][j] != 0) {// path blocked
					temp = -1;
					c = 10;
				}
			// special cases
			if (i < s.board.length - 1)// preceded by blank (-xx- or -xxx-)
				if (temp == 7 && s.board[i + 1][j] == 0)
					temp = 10000;
			if (!first && temp == 9)
				temp = 9990;
			score += temp;
			temp = 0;
		} else
			score--;

		if (i < s.board.length - 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i + c][j] == check)
					temp += 5 - c;
				else if (s.board[i + c][j] != 0) {// path blocked
					temp = -1;
					c = 10;
				}
			// special cases
			if (i > 0)// preceded by blank (-xx- or -xxx-)
				if (temp == 7 && s.board[i - 1][j] == 0)
					temp = 10000;
			if (!first && temp == 9)
				temp = 9990;
			score += temp;
			temp = 0;
		} else
			score--;

		if (j >= 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i][j - c] == check)
					temp += 5 - c;
				else if (s.board[i][j - c] != 0) {// path blocked
					temp = -1;
					c = 10;
				}
			// special cases
			if (j < s.board.length - 1)// preceded by blank (-xx- or -xxx-)
				if (temp == 7 && s.board[i][j + 1] == 0)
					temp = 10000;
			if (!first && temp == 9)
				temp = 9990;
			score += temp;
			temp = 0;
		} else
			score--;

		if (j < s.board.length - 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i][j + c] == check)
					temp += 5 - c;
				else if (s.board[i][j + c] != 0) {// path blocked
					temp = -1;
					c = 10;
				}
			// special cases
			if (j > 0)// preceded by blank (-xx- or -xxx-)
				if (temp == 7 && s.board[i][j - 1] == 0)
					temp = 10000;
			if (!first && temp == 9)
				temp = 9990;
			score += temp;
			temp = 0;
		} else
			score--;

		// check immediate diagonals, mostly to help break ties
		if (!first)
			if (i >= 1 && i < s.board.length - 1 && j >= 1
					&& j < s.board.length - 1) {
				if (s.board[i + 1][j + 1] == check
						|| s.board[i + 1][j - 1] == check
						|| s.board[i - 1][j + 1] == check
						|| s.board[i - 1][j - 1] == check)
					score++;
			}

		if (!first)
			check = s.board[i][j];
		return score * check;// negates if opponent
	}
}