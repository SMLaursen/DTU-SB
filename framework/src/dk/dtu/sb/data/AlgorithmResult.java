package dk.dtu.sb.data;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import dk.dtu.sb.spn.StochasticPetriNet;

/**
 * Data wrapper class for holding results of algorithm runs. Consists of a
 * linked list of {@link SimulationPoint}.
 */
public class AlgorithmResult {

    private LinkedList<SimulationPoint> data = new LinkedList<SimulationPoint>();

    /**
     * Default constructor with no side-effects.
     */
    public AlgorithmResult() {

    }

    /**
     * Adds a {@link SimulationPoint} with time 0 and the initial markings
     * given.
     * 
     * @param initialMarkings
     *            Usually this will be the data from
     *            {@link StochasticPetriNet#getInitialMarkings()}
     */
    public AlgorithmResult(Map<String, Integer> initialMarkings) {
        add(0, initialMarkings);
    }

    /**
     * Adds the given simulation point to the algorithm result.
     * 
     * @param point
     *            See {@link SimulationPoint}.
     */
    public void add(SimulationPoint point) {
        data.add(point);
    }

    /**
     * Adds the given time and markings as a {@link SimulationPoint} to the
     * algorithm result.
     * 
     * @param time
     *            The time.
     * @param markings
     *            The markings at the time.
     */
    public void add(double time, Map<String, Integer> markings) {
        data.add(new SimulationPoint(time, markings));
    }

    /**
     * All of the species involved in this result. Will get the species from
     * {@link SimulationPoint#getMarkings()}.
     */
    public Set<String> getSpecies() {
        return data.getFirst().getMarkings().keySet();
    }

    /**
     * Returns all the simulation points of this results.
     */
    public LinkedList<SimulationPoint> getSimulationPoints() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
