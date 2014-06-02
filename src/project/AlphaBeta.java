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

		for (Action a : children){
			state.move(a.i, a.j, false);
			if (evaluate(state) < v)
				children.remove(a); // eliminates all unnecessary states
			state.undo(a.i, a.j);
		}
		return children.get(0);
	}

	private int maxValue(State state) {
		// return utility value of st
		if (cutoff() || state.checkWin() != 0 || state.spaces <= 0
				|| depth == 0)
			return evaluate(state);
		int v = Integer.MIN_VALUE;
		for (Action a : children) {
			state.move(a.i, a.j, false);
			--depth;
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
		if (cutoff() || state.checkWin() != 0 || state.spaces <= 0
				|| depth == 0)
			return evaluate(state);
		int v = Integer.MAX_VALUE;
		for (Action a : children) {
			state.move(a.i,  a.j, true);
			--depth;
			v = Integer.min(v, maxValue(state));
			state.undo(a.i, a.j);
			if (v >= alpha)
				return v;
			beta = Integer.min(beta, v);
		}
		return v;
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
		for (int i = 0; i < s.board.length; ++i)
			for (int j = 0; j < s.board.length; ++j)
				if (s.board[i][j] != 0)
					for (int n = 1; n < 4; ++n) {
						if (i + n < s.board.length) {
							score++;
							if (s.board[i][j] == s.board[i + n][j])
								score += s.board[i][j];
						}
						if (j + n < s.board.length) {
							score++;
							if (s.board[i][j] == s.board[i][j + n])
								score += s.board[i][j];
						}
						if (i - n > 0) {
							score++;
							if (s.board[i][j] == s.board[i - n][j])
								score += s.board[i][j];
						}
						if (j - n > 0) {
							score++;
							if (s.board[i][j] == s.board[i][j - n])
								score += s.board[i][j];
						}
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
