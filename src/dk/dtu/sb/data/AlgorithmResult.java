package dk.dtu.sb.data;

import java.util.Collection;
import java.util.HashMap;
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
     * @param iterations
     * @param initialMarkings
     */
    public void reset(int iterations, Map<String, Integer> initialMarkings) {
        data.clear();
        for (int iteration = 0; iteration < iterations; iteration++) {
            data.add(new SimulationPoint(0, initialMarkings));
        }
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
}
