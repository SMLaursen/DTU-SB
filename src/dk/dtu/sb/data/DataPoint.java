package dk.dtu.sb.data;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class DataPoint<T> {

    private double time;
    private Map<String, T> markings = new HashMap<String, T>();
    
    public DataPoint(double time, Map<String, T> markings) {
        this.time = time;
        this.markings.putAll(markings);
    }
    
    /**
     * 
     * @return
     */
    public Map<String, T> getMarkings() {
        return markings;
    }
    
    /**
     * 
     * @return
     */
    public double getTime() {
        return time;
    }

    /**
     * 
     */
    public String toString() {
        return "[" + time + "] : " + markings;
    }
    
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof DataPoint)) return false;
        DataPoint<T> that = (DataPoint<T>)other;
        return time == that.getTime() && markings.equals(that.getMarkings());
    }
}
