package dk.dtu.sb.data;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * 
 */
public class AlgorithmResult {

    private LinkedList<SimulationPoint> data = new LinkedList<SimulationPoint>();

    /**
     * 
     */
    public AlgorithmResult() {
        
    }
    /**
     * 
     * @param initialMarkings
     */
    public AlgorithmResult(Map<String, Integer> initialMarkings) {
        add(0, initialMarkings);
    }
    
    /**
     * 
     * @param iteration
     * @param state
     */
    public void add(SimulationPoint state) {
        data.add(state);
    }

    /**
     * 
     * @param iteration
     * @param time
     * @param markings
     */
    public void add(double time, Map<String, Integer> markings) {
        data.add(new SimulationPoint(time, markings));
    }

    /**
     * 
     * @return
     */
    public Set<String> getSpecies() {
        return data.getFirst().getMarkings().keySet();
    }

    /**
     * 
     * @return
     */
    public LinkedList<SimulationPoint> getSimulationPoints() {
        return data;
    }
    
    public String toString() {
        return data.toString();
    }
}
