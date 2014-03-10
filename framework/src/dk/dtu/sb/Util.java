package dk.dtu.sb;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import dk.dtu.sb.spn.Reaction;

/**
 * Utility class with common methods.
 */
public class Util {

    /**
     * Logger-class, used for debugging and error-reporting.
     */
    public static Logger log = (Logger) LoggerFactory.getLogger("DTU-SB");

    /**
     * Implementation of binomial coefficient.
     * 
     * @return Returns the number of combinations for (n k)
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

    /**
     * Executes the reaction and updates the markings with reaction.
     * 
     * @param reaction
     *            The input reaction manipulating the markings.
     * @param markings
     *            The markings to change.
     */
    public static void updateMarkings(Reaction reaction,
            Map<String, Integer> markings) {
        int multiplicity, oldMarking;
        for (Entry<String, Integer> reactantEntry : reaction.getReactants()
                .entrySet()) {
            multiplicity = reactantEntry.getValue();
            oldMarking = markings.get(reactantEntry.getKey());
            if (oldMarking < multiplicity) {
                throw new RuntimeException(
                        "Performing update with fewer tokens than required.");
            }
            markings.put(reactantEntry.getKey(), oldMarking - multiplicity);
        }

        for (Entry<String, Integer> productEntry : reaction.getProducts()
                .entrySet()) {
            multiplicity = productEntry.getValue();
            oldMarking = markings.get(productEntry.getKey());
            markings.put(productEntry.getKey(), oldMarking + multiplicity);
        }
    }

    /**
     * Checks if the heap-size is large enough. The simulation can be quite
     * demanding.
     * 
     * @param megaBytes
     *            The minimum MB - recommended is at least 1GB.
     * @return Whether the heap size is less than the specified MB.
     */
    public static boolean isHeapSizeLessThan(long megaBytes) {
        return (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()
                .getMax() + (30L * 1024L * 1024L)) < (megaBytes * 1024L * 1024L);
    }

}