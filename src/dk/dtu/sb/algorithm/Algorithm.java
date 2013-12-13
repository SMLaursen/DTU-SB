package dk.dtu.sb.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.output.data.SimulationPoint;
import dk.dtu.sb.spn.Reaction;
import dk.dtu.sb.spn.StochasticPetriNet;

/**
 * 
 */
public class Algorithm implements Runnable {

    /**
     * This method will initiate the algorithm run. This is the only method that
     * needs to be overridden in order to implement a new algorithm.
     */
    public void run() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * The iteration index, for determining where to store output data
     */
    protected int iterationIndex;

    protected static double stoptime;
    protected static int rateMode;
    protected static double threshold;
    protected static Parameters params = new Parameters();
    protected static StochasticPetriNet spn = new StochasticPetriNet();

    private volatile static HashMap<Integer, LinkedList<SimulationPoint>> resultData = new HashMap<Integer, LinkedList<SimulationPoint>>();
    private static Object resultLock = new Object();

    /**
     * The current markings of the places in the {@link StochasticPetriNet}.
     * I.e. the places is the reactants and the products of the reactions in the
     * {@link StochasticPetriNet}.
     */
    protected HashMap<String, Integer> currentMarkings = new HashMap<String, Integer>(
            spn.getInitialMarkings());

    /**
     * 
     * 
     * @param iteration
     */
    public void setIteration(int iteration) {
        iterationIndex = iteration;
    }

    /**
     * Sets the shared input. This is used for all sub-classes of this class.
     * The output is cleared when this method is invoked.
     * 
     * @param spn
     * @param params
     */
    public static void setInput(StochasticPetriNet spn, Parameters params) {
        stoptime = params.getStoptime();
        rateMode = params.getRateMode();
        threshold = params.getSimThreshold();
        Algorithm.spn = spn;
        Algorithm.params = params;

        resetResult();
    }

    /**
     * 
     */
    public static void resetResult() {
        resultData.clear();
        for (int iteration = 0; iteration < params.getIterations(); iteration++) {
            resultData.put(iteration, new LinkedList<SimulationPoint>());
            resultData.get(iteration).add(
                    new SimulationPoint(0, spn.getInitialMarkings()));
        }
    }

    /**
     * Executes the reaction and updates the markings with reaction
     * 
     * @param reaction
     * @param previousMarkings
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
     * Get the final result of several runs of the algorithm.
     */
    public static HashMap<Integer, LinkedList<SimulationPoint>> getOutput() {
        synchronized (resultLock) {
            return resultData;
        }
    }

    /**
     * Add the result so far. Usually this method is used in
     * {@link Algorithm#run()} implemented by a sub-class of this class.
     * 
     * @param state
     *            See {@link SimulationPoint}.
     */
    protected void addPartialResult(SimulationPoint state) {
        synchronized (resultLock) {
            resultData.get(iterationIndex).add(state);
        }
    }

    /**
     * Clears data from a single iteration. Can be used to cleanup timeouted
     * threads.
     */
    protected void deletePartialResult() {
        synchronized (resultLock) {
            if (resultData.containsKey(iterationIndex)) {
                resultData.remove(iterationIndex);
            }
        }
    }
}
