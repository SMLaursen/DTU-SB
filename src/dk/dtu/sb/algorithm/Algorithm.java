package dk.dtu.sb.algorithm;

import java.util.Collection;
import java.util.HashMap;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.data.AlgorithmResult;
import dk.dtu.sb.data.SimulationPoint;
import dk.dtu.sb.spn.StochasticPetriNet;

/**
 * This class implements a bunch of helper methods that can be used in the
 * method {@link #run()} that should be implemented when this class is extended.
 */
public abstract class Algorithm implements Runnable {

    private final int uniqueId;

    /**
     * The current time. This is typically incremented in {@link #run()}.
     */
    protected double time = 0.0;

    /**
     * The current markings of the places in the {@link StochasticPetriNet},
     * i.e. the places is the reactants and the products of the reactions in the
     * {@link StochasticPetriNet}.
     */
    protected HashMap<String, Integer> currentMarkings = new HashMap<String, Integer>(
            spn.getInitialMarkings());

    /**
     * 
     */
    protected static Parameters params = new Parameters();

    /**
     * 
     */
    protected static StochasticPetriNet spn = new StochasticPetriNet();

    /**
     * 
     */
    private static volatile HashMap<Integer, AlgorithmResult> result = new HashMap<Integer, AlgorithmResult>();

    /**
     * Default constructor generating the uniqueid and creating the
     * {@link AlgorithmResult} of this run.
     */
    public Algorithm() {
        uniqueId = hashCode();
        result.put(uniqueId, new AlgorithmResult(spn.getInitialMarkings()));
    }

    /**
     * This method will initiate the algorithm run. This is the only method that
     * needs to be overridden in order to implement a new algorithm.
     */
    public void run() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Sets the shared input. This is used for all sub-classes of this class.
     * 
     * @param spn
     *            The shared {@link StochasticPetriNet} to be used by all
     *            iterations of the simulations.
     * @param params
     *            The shared {@link Parameters} to be used by all iterations of
     *            the simulations.
     */
    public static void setInput(StochasticPetriNet spn, Parameters params) {
        Algorithm.spn = spn;
        Algorithm.params = params;
    }
    
    /**
     * Used to set the {@link Parameters} object after instantiation.
     * 
     * @param params
     *            The {@link Parameters} with additional simulator parameters
     *            specified.
     */
    public static void setParams(Parameters params) {
        Algorithm.params = params;
    }

    /**
     * Get the final result of several runs of the algorithm.
     */
    public static Collection<AlgorithmResult> getOutput() {
        return result.values();
    }

    /**
     * Add the result so far. Usually this method is used in
     * {@link Algorithm#run()} implemented by a sub-class of this class.
     * 
     * @param state
     *            See {@link SimulationPoint}.
     */
    protected void addPartialResult(SimulationPoint state) {
        result.get(uniqueId).add(state);
    }

    /**
     * See {@link #addPartialResult(SimulationPoint)}. Here the current
     * {@link #time} and {@link #currentMarkings} is automatically used, i.e.
     * the {@link SimulationPoint} is automatically created.
     */
    protected void addPartialResult() {
        result.get(uniqueId).add(time, currentMarkings);
    }
}
