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
	private List<State> successors = new ArrayList<State>();
	private long st;
	private long lim;

	public AB(int lim) {
		this.lim = (long) (lim*1000);
	}

	public State absearch(State state) {
		st = System.currentTimeMillis();
		generateSuccessors(state);
		a = Integer.MIN_VALUE;
		b = Integer.MAX_VALUE;
		int v = maxValue(state);
		return null; //return action from successors with value v
	}

	private int maxValue(State state) {
		if(cutoff())// if () terminal test
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
		if(cutoff())// if () terminal test
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
		
		
	}
	
	/**
	 * Evaluate non-terminal states
	 * 
	 * @param s
	 * @return
	 */
	private int evaluate(State s) {
		
		////
		////
		return 0;
		
	}
	
	/**
	 * Program will return the best solution found so far given a specific period of time
	 * @return
	 */
	private boolean cutoff() {
		if (System.currentTimeMillis() - st > lim)
			return true;
		return false;
	}
}
