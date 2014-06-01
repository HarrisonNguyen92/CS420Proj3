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

	public AlphaBeta(int lim) {
		this.limit = (long) (lim * 1000);
	}

	public Action absearch(State state) {
		startTime = System.currentTimeMillis();
		generateSuccessors(state);
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		int v = maxValue(state);
		for (Action move : children)
			if (evaluate(state.fakeMove(move.i, move.j, false),
					state.spaces - 1) >= v)
				return move;
		return children.get(0); // return action from successors with value v
	}

	private int maxValue(State state) {
		if (cutoff() || state.checkWin() != 0 || state.spaces <= 0)
			return evaluate(state.board, state.spaces);// return utility value
														// of st
		int v = Integer.MIN_VALUE;
		for (Action move : children) {
			v = Integer.max(
					v,
					evaluate(state.fakeMove(move.i, move.j, false),
							state.spaces - 1));
			if (v >= beta)
				return v;
			alpha = Integer.max(alpha, v);
		}
		return v;
	}

	private int minValue(State state) {
		if (cutoff() || state.checkWin() != 0 || state.spaces <= 0)
			return evaluate(state.board, state.spaces);// return utility value
														// of st
		int v = Integer.MAX_VALUE;
		for (Action move : children) {
			v = Integer.min(
					v,
					evaluate(state.fakeMove(move.i, move.j, true),
							state.spaces - 1));
			if (v >= alpha)
				return v;
			beta = Integer.min(beta, v);
		}
		return v;
	}

	private void generateSuccessors(State s) {
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
	private int evaluate(int[][] init, int spaces) {
		State s = new State(init, spaces);
		if (s.checkWin() == 1)
			return Integer.MAX_VALUE;
		if (s.checkWin() == -1)
			return Integer.MIN_VALUE;
		if (s.spaces == 0)
			return 0;
		return 0;
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
