package dk.dtu.sb.algorithm;

import java.util.HashMap;
import java.util.LinkedList;

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
	private volatile static LinkedList<ReactionEvent> resultData = new LinkedList<ReactionEvent>();
	/**
	 * 
	 */
	protected HashMap<String, Integer> currentMarkings;
	
	/**
	 * 
	 */
	protected static StochasticPetriNet spn;
	
	/**
	 * 
	 * @param spn
	 * @param stoptime
	 */
	public static void initialize(StochasticPetriNet spn, double stoptime){
		
		Algorithm.stoptime = stoptime;
		Algorithm.spn = spn;
		resultData.clear();
	}
	
	/**
	 * 
	 */
	public void run() {
		throw new UnsupportedOperationException("Not implemented.");
	}
	
    /**
     * Executes the reaction and updates the markings with r
     * 
     * @param reaction
     * @param previousMarkings
     */
    public static synchronized void updateMarkings(Reaction reaction,
            HashMap<String, Integer> previousMarkings) {
        // Reactants
        for (Species reactant : reaction.getReactants().values()) {
            int multiplicity = reactant.getMultiplicity();
            int old = previousMarkings.get(reactant.getId());

            if (old < multiplicity) {
                throw new RuntimeException(
                        "ERROR : performing update with fewer tokens than required");
            }
            // Overwrite old value with updated
            previousMarkings.put(reactant.getId(), old - multiplicity);
        }

        // Products
        for (Species product : reaction.getProducts().values()) {
            int multiplicity = product.getMultiplicity();
            int old = previousMarkings.get(product.getId());
            
            // Overwrite old value with updated
            previousMarkings.put(product.getId(), old + multiplicity);
        }
    }
    
	
    public static synchronized LinkedList<ReactionEvent> getPlotData(){
			return resultData;
	}
	
	
	public static synchronized void addPartialResult(ReactionEvent r){
			resultData.add(r);
	}
}
