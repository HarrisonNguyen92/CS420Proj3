/**
 * 
 */
package project;

/**
 * @author Harrison
 * 
 */
public class Action {
	protected int i;
	protected int j;

	public Action(int i, int j) {
		this.i = i;
		this.j = j;
	}

	public void print() {
		System.out.println("Move: (" + i + ", " + j + ")\n");
	}
}
