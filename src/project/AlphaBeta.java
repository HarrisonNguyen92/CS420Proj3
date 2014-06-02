/**
 * 
 */
package project;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harrison
 * 
 */
public class AlphaBeta {
	private int alpha;
	private int beta;
	private List<Action> children = new ArrayList<Action>();
	private long startTime;
	private long limit;
	private int depth;

	public AlphaBeta(int lim) {
		this.limit = (long) (lim * 1000);
	}

	public Action absearch(State state) {
		depth = (int) limit;
		startTime = System.currentTimeMillis();
		generateSuccessors(state);
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;

		int v = maxValue(state);

		for (int t = 0; t < children.size(); ++t) {
			Action a = children.get(t);
			state.move(a.i, a.j, false);
			if (evaluate(state) < v)
				children.remove(t); // eliminates all unnecessary states
			state.undo(a.i, a.j);
		}
		return children.get(0);
	}

	private int maxValue(State state) {
		// return utility value of st
		if (cutoff() || state.checkWin() != 0 || state.spaces <= 0 || depth < 0)
			return evaluate(state);
		int v = Integer.MIN_VALUE;
		--depth;
		for (Action a : children) {
			state.move(a.i, a.j, false);
			v = Integer.max(v, minValue(state));
			state.undo(a.i, a.j);
			if (v >= beta)
				return v;
			alpha = Integer.max(alpha, v);
		}
		return v;
	}

	private int minValue(State state) {
		// return utility value of st
		if (cutoff() || state.checkWin() != 0 || state.spaces <= 0 || depth < 0)
			return evaluate(state);
		int v = Integer.MAX_VALUE;
		--depth;
		for (Action a : children) {
			state.move(a.i, a.j, true);
			v = Integer.min(v, maxValue(state));
			state.undo(a.i, a.j);
			if (v >= alpha)
				return v;
			beta = Integer.min(beta, v);
		}
		return v;
	}

	public Action makeMove(State state) {
		int best = -20000;
		long startTime = System.currentTimeMillis();
		int score;
		int mi = 0;
		int mj = 0;
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board.length; j++) {
				if (state.board[i][j] == 0) {
					state.board[i][j] = 1; // make move on board
					score = min(state, startTime, best);
					if (score > best) {
						mi = i;
						mj = j;
						best = score;
					}
					state.board[i][j] = 0; // undo move
				}
			}
		}
		System.out.println(best);
		return new Action(mi, mj);
	}

	private int max(State state, double st) {
		int best = -20000;
		int score;
		if (state.checkWin() != 0)
			return state.checkWin();
		if (System.currentTimeMillis() - st >= limit)
			return evaluate(state);
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board.length; j++) {
				if (state.board[i][j] == 0) {
					state.board[i][j] = 1; // make move
					score = min(state, st, best);
					if (score > best)
						best = score;
					state.board[i][j] = 0;
				}
			}
		}
		return best;
	}

	private int min(State state, double st, int prune) {
		int best = 20000;
		int score;
		if (state.checkWin() != 0)
			return state.checkWin();
		if (System.currentTimeMillis() - st >= limit)
			return evaluate(state);
		for (int i = 0; i < state.board.length; i++) {
			for (int j = 0; j < state.board.length; j++) {
				if (state.board[i][j] == 0) {
					state.board[i][j] = -1; // make move
					score = max(state, st);
					if (score < best)
						best = score;
					state.board[i][j] = 0;
					if (score < prune)
						return score;
				}
			}
		}
		return best;
	}

	private void generateSuccessors(State s) {
		children.clear();
		for (int i = 0; i < s.board.length; ++i)
			for (int j = 0; j < s.board[i].length; ++j)
				if (s.board[i][j] == 0) {
					children.add(new Action(i, j));
				}
	}

	/**
	 * Evaluate non-terminal states
	 * 
	 * @param s
	 * @return
	 */
	private int evaluate(State s) {
		if (s.checkWin() == 1)
			return Integer.MAX_VALUE / 4;
		if (s.checkWin() == -1)
			return Integer.MIN_VALUE / 4;
		if (s.spaces == 0)
			return 0;
		int score = 0;
		int xCount = 0; // variable to keep track of Xs in a row
		int oCount = 0; // variable to keep track of Os in a row
		for (int i = 0; i < s.board.length; ++i) {
			for (int j = 0; j < s.board.length; ++j) {
				if (s.board[i][j] == -1) {
					oCount++;
					score += xCount - 1;
					xCount = 0;
				}
				if (s.board[i][j] == 1) {
					if (i < 2 || j < 2 || i > s.board.length - 3
							|| j > s.board.length - 3)
						xCount -= 2; // screw the walls
					xCount++;
					score -= oCount - 1;
					oCount = 0;
				}
				if (s.board[i][j] == 0) {
					if (j != 0)
						if (s.board[i][j - 1] == -1)
							oCount += 1;
					if (j != s.board.length - 1)
						if (s.board[i][j + 1] == -1)
							oCount += 1;
					if (oCount > 2)
						oCount *= 2;
					if (oCount > 1 || xCount > 1)
						score += xCount - oCount;
					oCount = 0;
					xCount = 0;
				}
			}
			xCount = 0;
			oCount = 0;
		}
		for (int j = 0; j < s.board.length; ++j) {
			for (int i = 0; i < s.board.length; ++i) {
				if (s.board[i][j] == -1) {
					oCount++;
					score += xCount - 1;
					xCount = 0;
				}
				if (s.board[i][j] == 1) {
					xCount++;
					score -= oCount - 1;
					oCount = 0;
				}
				if (s.board[i][j] == 0) {
					if (i != 0)
						if (s.board[i - 1][j] == -1)
							oCount += 1;
					if (i != s.board.length - 1)
						if (s.board[i + 1][j] == -1)
							oCount += 1;
					if (oCount > 2)
						oCount *= 2;
					if (oCount > 1 || xCount > 1)
						score += xCount - oCount;
					oCount = 0;
					xCount = 0;
				}
			}
			xCount = 0;
			oCount = 0;
		}
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
