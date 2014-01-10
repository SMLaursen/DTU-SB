package dk.dtu.sb.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This general class represents markings, or concentrations, of a time point.
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

    /**
     * Get the species involved in this point.
     */
    public Set<String> getSpecies() {
        return markings.keySet();
    }

    /**
     * Get the value of the species for this time point.
     * 
     * @param species
     *            The name of the species to find.
     */
    public T getMarking(String species) {
        if (!markings.containsKey(species)) {
            throw new RuntimeException("No species with this name: " + species);
        }
        return markings.get(species);
    }

    @Override
    public String toString() {
        return "[" + time + "] : " + markings;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof DataPoint))
            return false;
        if (other == this)
            return true;
        DataPoint<?> that = (DataPoint<?>) other;
        return time == that.getTime() && markings.equals(that.getMarkings());
    }
}
