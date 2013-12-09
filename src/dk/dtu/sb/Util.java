package dk.dtu.sb;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * Utility class with common methods.
 */
public class Util {
	
    /**
     * Logger-class, used for debugging and error-reporting.
     */
    public static final Logger log = (Logger) LoggerFactory.getLogger("DTU-SB");

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