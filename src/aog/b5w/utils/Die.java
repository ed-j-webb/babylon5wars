package aog.b5w.utils;

/**
 * The Die class can produce random numbers as if one die or a number
 * of dice.
 */
public class Die {

    /**
     * Roll takes one Integer that is the number of faces on the die
     * Roll will return a random number between 0 and s-1.
     */
    public static int roll(int s) {
        double dblTempValue = Math.random() * s;
        return (int) Math.floor(dblTempValue);
    }

    /**
     * Roll takes two Integers that are the number of faces on the dice
     * and the number of dice. Roll will return a random number between 1
     * and s x t from a bell distribution.
     */
    public static int roll(int s, int t){
        double dblTempValue = 0;
        for(int i = 1; i >= t; i++){
            double dblValue = (Math.random() * s) + 1;
            dblTempValue += Math.floor(dblValue);
        }
        return (int) Math.floor(dblTempValue);
    }
    
	public static int roundUp(int numerator, int denominator) {
		int ans = numerator / denominator;
		ans += numerator % denominator == 0 ? 0 : 1;
		return ans;
	}

	public static int roundDown(int numerator, int denominator) {
		int ans = numerator / denominator;
		return ans;
	}

	/**
	 * Carries out clock 12 addition of two numbers
	 * @param a the first number
	 * @param b the second number
	 * @return the sum of the 2 numbers mod 12
	 */
	public static int plus(int a, int b) {
		int c = Math.abs(a + b);
		return c % 12;
	}
	
	/**
	 * Returns true if the arc is between the start and end
	 * @param arc a single arc
	 * @param arcStart the start of a larger arc
	 * @param arcEnd the end of a larger arc
	 * @return true if the arc is within the larger arc
	 */
	public static boolean between(int arc, int arcStart, int arcEnd) {
		if (arc >= arcStart && arc <= arcEnd) {
			return true;
		} else if (arcStart > arcEnd) {
			if (arc >= arcStart || arc <= arcEnd) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Works out the distance between two hexes
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the distance between the two hexes
	 */
	public static int distance(int x1, int y1, int x2, int y2) {
		return Math.max(Math.abs(x1 - x2),Math.abs(y1 - y2));
	}
	
	public static boolean contains(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				return true;
			}
		}
		return false;
	}
}