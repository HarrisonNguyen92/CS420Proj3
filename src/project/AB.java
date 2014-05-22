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

	public AB() {
		a = Integer.MIN_VALUE;
		b = Integer.MAX_VALUE;
	}

	public Action absearch(State state) {
		int v = maxValue(state);
		return null; //return action from successors with value v
	}

	private int maxValue(State state) {
		// if () terminal test
		// return utility value of st
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
		// if () terminal test
		// return utility value of st
		int v = Integer.MAX_VALUE;
		for (State s : successors) {
			v = Integer.min(v, maxValue(s));
			if (v >= a)
				return v;
			b = Integer.min(b, v);
		}
		return v;
	}
}
