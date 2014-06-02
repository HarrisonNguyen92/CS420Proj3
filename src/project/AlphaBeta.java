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
	private long limit;
	
	/**
	 * depth of the algorithm (to be used with IDDFS)
	 */
	private int depth;

	public AlphaBeta(int lim) {
		this.limit = (long) (lim * 1000);
	}

	public Action absearch(State state) {
		depth = Integer.MAX_VALUE;
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
					score = minValue(state);
					if (score > best) {
						mi = i;
						mj = j;
						best = score;
					}
					state.undo(i, j); // undo move
				}
				// The only problem that i have with the above statement is that
				// it will take the FIRST best solution. If we can keep a record
				// of all best solutions and then make a random choice, it may
				// be better.
			}
		}
		System.out.println(best);
		return new Action(mi, mj);
	}

	private int maxValue(State state) {
		// if (state.checkWin() != 0) return state.checkWin(); i dont think
		// returning a simple 1 / -1 will suffice when comparing to other
		// evaluation returns
		if (cutoff() || depth <= 0 || state.spaces <= 0
				|| state.checkWin() != 0)
			return evaluate(state);

		int best = alpha;// best = -20000
		depth--;
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board.length; j++) {
				if (state.board[i][j] == 0) {
					state.move(i, j, true);// make move
					best = Integer.max(best, minValue(state));
					// the line above is basically
					// score = max(state, st, best);
					// if (score < best)
					// best = score;
					state.undo(i, j);
					if (best > beta)//pruning
						return beta;
				}
			}
		}
		return best;
	}

	private int minValue(State state) {
		// if (state.checkWin() != 0) return state.checkWin(); i dont think
		// returning a simple 1 / -1 will suffice when comparing to other
		// evaluation returns
		if (cutoff() || depth <= 0 || state.spaces <= 0
				|| state.checkWin() != 0)
			return evaluate(state);

		int best = beta; // best = 20000
		depth--;
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board.length; j++) {
				if (state.board[i][j] == 0) {
					state.move(i, j, false);// make move
					best = Integer.min(best, maxValue(state));
					// the line above is basically
					// score = min(state, st, best);
					// if (score > best)
					// best = score;
					state.undo(i, j);
					if (best < alpha)//pruning
						return alpha;
				}
			}
		}
		return best;
	}

	/**
	 * Evaluate non-terminal states
	 * 
	 * @param s
	 * @return
	 */
	private int evaluate(State s) {
		// an example eval f(n) for tic tac toe was
		// (#3lengths open for me) - (#3lengths open for you)
		if (s.checkWin() == 1)
			return Integer.MAX_VALUE / 4;
		if (s.checkWin() == -1)
			return Integer.MIN_VALUE / 4;
		if (s.spaces == 0)
			return 0;
		int score = 0;
		int xCount = 0; // variable to keep track of Xs in a row
		int oCount = 0; // variable to keep track of Os in a row
		// checks for all vertical rows possible
		for (int i = 0; i < s.board.length; ++i)
			for (int j = 0; j < s.board.length - 3; ++j) {
				for (int k = j; k < j + 4; ++k) {
					if (s.board[i][k] == 1)
						xCount++;
					if (s.board[i][k] == -1)
						oCount++;
				}
				if (!(xCount > 0 && oCount > 0)) {
					if (xCount > 0)
						score += xCount;
					if (oCount > 0)
						score -= oCount;
					xCount = 0;
					oCount = 0;
				}
			}
		xCount = 0;
		oCount = 0;
		// checks for all horizontal rows possible
		for (int i = 0; i < s.board.length - 3; ++i)
			for (int j = 0; j < s.board.length; ++j) {
				for (int k = i; k < i + 4; ++k) {
					if (s.board[k][j] == 1)
						xCount++;
					if (s.board[k][j] == -1)
						oCount++;
				}
				if (!(xCount > 0 && oCount > 0)) {
					if (xCount > 0)
						score += xCount;
					if (oCount > 0)
						score -= oCount;
					xCount = 0;
					oCount = 0;
				}
			}

		/*
		 * for (int i = 0; i < s.board.length; ++i) { for (int j = 0; j <
		 * s.board.length; ++j) { if (s.board[i][j] == -1) { oCount++; score +=
		 * xCount - 1; xCount = 0; } if (s.board[i][j] == 1) { if (i < 2 || j <
		 * 2 || i > s.board.length - 3 || j > s.board.length - 3) xCount -= 2;
		 * // screw the walls xCount++; score -= oCount - 1; oCount = 0; } if
		 * (s.board[i][j] == 0) { if (j != 0) if (s.board[i][j - 1] == -1)
		 * oCount += 1; if (j != s.board.length - 1) if (s.board[i][j + 1] ==
		 * -1) oCount += 1; if (oCount > 2) oCount *= 2; if (oCount > 1 ||
		 * xCount > 1) score += xCount - oCount; oCount = 0; xCount = 0; } }
		 * xCount = 0; oCount = 0; }
		 * 
		 * for (int j = 0; j < s.board.length; ++j) { for (int i = 0; i <
		 * s.board.length; ++i) { if (s.board[i][j] == -1) { oCount++; score +=
		 * xCount - 1; xCount = 0; } if (s.board[i][j] == 1) { xCount++; score
		 * -= oCount - 1; oCount = 0; } if (s.board[i][j] == 0) { if (i != 0) if
		 * (s.board[i - 1][j] == -1) oCount += 1; if (i != s.board.length - 1)
		 * if (s.board[i + 1][j] == -1) oCount += 1; if (oCount > 2) oCount *=
		 * 2; if (oCount > 1 || xCount > 1) score += xCount - oCount; oCount =
		 * 0; xCount = 0; } } xCount = 0; oCount = 0; }
		 */
		return score;
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
}
