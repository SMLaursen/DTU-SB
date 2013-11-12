package dk.dtu.sb.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import dk.dtu.sb.data.Reaction;
import dk.dtu.sb.data.ReactionEvent;
import dk.dtu.sb.data.Species;
import dk.dtu.sb.data.StochasticPetriNet;

/**
 * 
 */
public class Algorithm implements Runnable {

    /**
     * 
     */
	protected static double stoptime;
	
	/**
	 * 
	 */
	private volatile static LinkedList<ReactionEvent> resultData = new LinkedList<ReactionEvent>();
	
	/**
	 * The current markings of the places in the {@link StochasticPetriNet}. 
	 * I.e. the places is the reactants and the products of the reactions in the
	 * {@link StochasticPetriNet}.
	 */
	protected HashMap<String, Integer> currentMarkings = new HashMap<String, Integer>();
	
	/**
	 * 
	 */
	protected static StochasticPetriNet spn;
	
	/**
	 * Default constructor initialising current markings with initial markings 
	 * from the {@link StochasticPetriNet}.
	 */
	public Algorithm() {
	    currentMarkings.putAll(Algorithm.spn.getInitialMarkings());
	}
	
	/**
	 * Set the input.
	 * 
	 * @param spn {@link StochasticPetriNet}.
	 * @param stoptime The maximum of the last time point of the simulation.
	 */
	public static void setInput(StochasticPetriNet spn, double stoptime){		
		Algorithm.stoptime = stoptime;
		Algorithm.spn = spn;
		resultData.clear();
	}
	
	/**
	 * This method will initiate the algorithm run.
	 */
	public void run() {
		throw new UnsupportedOperationException("Not implemented.");
	}
	
    /**
     * Executes the reaction and updates the markings with reaction
     * 
     * @param reaction
     * @param previousMarkings
     */
    public static synchronized void updateMarkings(Reaction reaction,
            Map<String, Integer> markings) {
        int multiplicity, oldMarking;
        for (Entry<String, Integer> reactantEntry : reaction.getReactants().entrySet()) {
            multiplicity = reactantEntry.getValue();
            oldMarking = markings.get(reactantEntry.getKey());
            if (oldMarking < multiplicity) {
                throw new RuntimeException(
                        "Performing update with fewer tokens than required.");
            }
            markings.put(reactantEntry.getKey(), oldMarking - multiplicity);
        }

        for (Entry<String, Integer> productEntry : reaction.getProducts().entrySet()) {
            multiplicity = productEntry.getValue();
            oldMarking = markings.get(productEntry.getKey());            
            markings.put(productEntry.getKey(), oldMarking + multiplicity);
        }
    }
    
	/**
	 * Get the output.
	 * 
	 * @return
	 */
    public static synchronized LinkedList<ReactionEvent> getOutput(){
			return resultData;
	}
	
	/**
	 * Set the output
	 * 
	 * @param reactionEvent See {@link ReactionEvent}.
	 */
	public static synchronized void addPartialResult(ReactionEvent reactionEvent){
			resultData.add(reactionEvent);
	}
}
