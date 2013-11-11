package dk.dtu.sb;

import org.apache.commons.logging.impl.SimpleLog;

/**
 * Utility class with common methods.
 */
public class Util {
	
    /**
     * 
     */
    public static  SimpleLog log = new SimpleLog("DTU-SB");

    /**
     * Returns the number of combinations for (n k)
     * 
     * @param n
     * @param k
     * @return
     */
    public static int binom(int n, int k) {
        // Avoid factorial calculations
        long coeff = 1;
        for (int i = n - k + 1; i <= n; i++) {
            coeff *= i;
        }
        for (int i = 1; i <= k; i++) {
            coeff /= i;
        }
        return (int) coeff;
    }

}