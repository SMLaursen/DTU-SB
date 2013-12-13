package dk.dtu.sb.data;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents markings, or concentrations, of a time point.
 */
public class DataPoint<T> {

    private double time;
    private Map<String, T> markings = new HashMap<String, T>();

    /**
     * Default constructor creating a data point.
     * 
     * @param time
     *            The time.
     * @param markings
     *            The markings for the time.
     */
    public DataPoint(double time, Map<String, T> markings) {
        this.time = time;
        this.markings.putAll(markings);
    }

    /**
     * The markings for the {@link #getTime()}.
     */
    public Map<String, T> getMarkings() {
        return markings;
    }

    /**
     * The time.
     */
    public double getTime() {
        return time;
    }
    
    public String toString() {
        return "[" + time + "] : " + markings;
    }

    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof DataPoint))
            return false;
        DataPoint<T> that = (DataPoint<T>) other;
        return time == that.getTime() && markings.equals(that.getMarkings());
    }
}
