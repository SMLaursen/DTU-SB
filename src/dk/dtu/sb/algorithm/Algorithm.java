package dk.dtu.sb.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.data.AlgorithmResult;
import dk.dtu.sb.data.SimulationPoint;
import dk.dtu.sb.spn.Reaction;
import dk.dtu.sb.spn.StochasticPetriNet;

/**
 * 
 */
public class Algorithm implements Runnable {

    /**
     * 
     */
    protected double time = 0.0;

    /**
     * The current markings of the places in the {@link StochasticPetriNet},
     * i.e. the places is the reactants and the products of the reactions in the
     * {@link StochasticPetriNet}.
     */
    protected HashMap<String, Integer> currentMarkings = new HashMap<String, Integer>(
            spn.getInitialMarkings());

    protected static Parameters params = new Parameters();
    protected static StochasticPetriNet spn = new StochasticPetriNet();

    private AlgorithmResult algorithmResult = new AlgorithmResult(spn.getInitialMarkings());

    /**
     * This method will initiate the algorithm run. This is the only method that
     * needs to be overridden in order to implement a new algorithm.
     */
    public void run() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Sets the shared input. This is used for all sub-classes of this class.
     * The output is cleared when this method is invoked.
     * 
     * @param spn
     * @param params
     */
    public static void setInput(StochasticPetriNet spn, Parameters params) {
        Algorithm.spn = spn;
        Algorithm.params = params;
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
    public AlgorithmResult getOutput() {
        return algorithmResult;
    }
    
    /**
     * Add the result so far. Usually this method is used in
     * {@link Algorithm#run()} implemented by a sub-class of this class.
     * 
     * @param state
     *            See {@link SimulationPoint}.
     */
    protected void addPartialResult(SimulationPoint state) {
        algorithmResult.add(state);
    }

    /**
     * See {@link #addPartialResult(SimulationPoint)}. Here the current
     * {@link #time} and {@link #currentMarkings} is automatically used, i.e.
     * the {@link SimulationPoint} is automatically created.
     */
    protected void addPartialResult() {
        algorithmResult.add(time, currentMarkings);
    }
}
