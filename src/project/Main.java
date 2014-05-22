/**
 * 
 */
package project;

/**
 * @author Harrison
 * 
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[][] b = new String[8][8];
		for (int i = 0; i < b.length; ++i)
			for (int j = 0; j < b[i].length; ++j)
				b[i][j] = Character.toString((char) (i + 97)) + (j + 1);
		for (int i = 0; i < b.length; ++i) {
			for (int j = 0; j < b[i].length; ++j)
				System.out.print(b[i][j] + " ");
			System.out.println();
		}
		Action a = convert("b5");
		System.out.println("b5 converts to " + a.i + ", " + a.j);
		System.out.println(a.i + ", " + a.j + " converts to " + convert(a));

	}
	
	//I = how far down the board, J = how far right

	public static Action convert(String s) {
		// a = /u97, 0 = /u48
		return new Action(s.charAt(0) - 97, s.charAt(1) - 48 - 1);
	}

	public static String convert(Action a) {
		return Character.toString((char) (a.i + 97))
				+ Character.toString((char) (a.j + 49));
	}
}
