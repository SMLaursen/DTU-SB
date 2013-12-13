package dk.dtu.sb.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * 
 */
public class SimulationData {

    private HashMap<Integer, LinkedList<SimulationPoint>> data = new HashMap<Integer, LinkedList<SimulationPoint>>();

    /**
     * 
     * @param iteration
     * @param state
     */
    public void add(int iteration, SimulationPoint state) {
        data.get(iteration).add(state);
    }

    /**
     * 
     * @param iteration
     * @param time
     * @param markings
     */
    public void add(int iteration, double time, Map<String, Integer> markings) {
        data.get(iteration).add(new SimulationPoint(time, markings));
    }

    /**
     * 
     * @param iteration
     */
    public void clear(int iteration) {
        if (data.containsKey(iteration)) {
            data.remove(iteration);
        }
    }

    /**
     * 
     * @param iterations
     * @param initialMarkings
     */
    public void reset(int iterations, Map<String, Integer> initialMarkings) {
        data.clear();
        for (int iteration = 0; iteration < iterations; iteration++) {
            data.put(iteration, new LinkedList<SimulationPoint>());
            data.get(iteration).add(new SimulationPoint(0, initialMarkings));
        }
    }

    /**
     * 
     * @return
     */
    public Set<String> getSpecies() {
        return data.values().iterator().next().peekFirst().getMarkings()
                .keySet();
    }

    /**
     * 
     * @return
     */
    public Collection<LinkedList<SimulationPoint>> getAllSimulationPoints() {
        return data.values();
    }
}
