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
				// Potential random choice area. need list and PRNG
			}
		}
		System.out.println(best);
		return new Action(mi, mj);
	}

	private int maxValue(State state) {
		if (cutoff() || depth <= 0 || state.spaces <= 0
				|| state.checkWin() != 0)
			return eval(state);

		int best = alpha;
		depth--;
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
		if (cutoff() || depth <= 0 || state.spaces <= 0
				|| state.checkWin() != 0)

			return eval(state);
		int best = beta;
		depth--;
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

		// checks for all vertical rows possible
		for (int i = 0; i < s.board.length; ++i)
			for (int j = 0; j < s.board.length - 3; ++j) {
				for (int k = 0; k < 4; ++k) {
					if (s.board[i][j + k] == 1)
						x++;
					if (s.board[i][j + k] == -1)
						o++;
				}
				if (o < x)
					score -= x;
				if (x < o)
					score += o;
				score += x * x;
				score -= o * o;
				x = 0;
				o = 0;
			}

		// checks for all horizontal rows possible
		for (int i = 0; i < s.board.length - 3; ++i)
			for (int j = 0; j < s.board.length; ++j) {
				for (int k = 0; k < 4; ++k) {
					if (s.board[i + k][j] == 1)
						x++;
					if (s.board[i + k][j] == -1)
						o++;
				}
				if (o < x)
					score -= x;
				if (x < o)
					score += o;
				score += x * x;
				score -= o * o;
				x = 0;
				o = 0;
			}
		return score;
	}
}