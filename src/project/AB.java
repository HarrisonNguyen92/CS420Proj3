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
public class AB {
	private int a;
	private int b;
	private List<Action> successors = new ArrayList<Action>();
	private long st;
	private long lim;

	public AB(int lim) {
		this.lim = (long) (lim * 1000);
	}

	public Action absearch(State state) {
		st = System.currentTimeMillis();
		generateSuccessors(state);
		a = Integer.MIN_VALUE;
		b = Integer.MAX_VALUE;
		int v = maxValue(state);
		for (Action move : successors)
			if (evaluate(state.move(move.i, move.j, false)) < v)
				successors.remove(move);
		return successors.get(0); // return action from successors with value v
	}

	private int maxValue(State state) {
		if (cutoff() || state.checkWin() != 0 || state.spaces <= 0)
			return evaluate(state);// return utility value of st
		int v = Integer.MIN_VALUE;
		for (State s : successors) {
			v = Integer.max(v, minValue(s));
			if (v >= b)
				return v;
			a = Integer.max(a, v);
		}
		return v;
	}

	private int minValue(State state) {
		if (cutoff() || state.checkWin() != 0 || state.spaces <= 0)
			return evaluate(state);// return utility value of st
		int v = Integer.MAX_VALUE;
		for (State s : successors) {
			v = Integer.min(v, maxValue(s));
			if (v >= a)
				return v;
			b = Integer.min(b, v);
		}
		return v;
	}

	private void generateSuccessors(State s) {
		for (int i = 0; i < s.board.length; ++i)
			for (int j = 0; j < s.board[i].length; ++j)
				if (s.board[i][j] == 0) {
					successors.add(new Action(i, j));
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
		if (System.currentTimeMillis() - st > lim)
			return true;
		return false;
	}
}
