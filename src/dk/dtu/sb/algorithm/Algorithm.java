package dk.dtu.sb.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.output.data.DataPoint;
import dk.dtu.sb.spn.Reaction;
import dk.dtu.sb.spn.StochasticPetriNet;

/**
 * 
 */
public class Algorithm implements Runnable {

    /**
     * 
     */
	protected volatile static int i = 0;
	
	/**
	 * 
	 */
	protected static final Object iLock = new Object();

	/**
	 * 
	 */
	protected static double stoptime;

	/**
	 * 
	 */
	protected static int rateMode;

	/**
	 * 
	 */
	protected static double threshold;

	/**
	 * 
	 */
	private volatile static HashMap<Integer,LinkedList<DataPoint>> resultData = new HashMap<Integer, LinkedList<DataPoint>>();
	
	/**
	 * 
	 */
	private static Object mapLock = new Object();

	/**
	 * The current markings of the places in the {@link StochasticPetriNet}. 
	 * I.e. the places is the reactants and the products of the reactions in the
	 * {@link StochasticPetriNet}.
	 */
	protected HashMap<String, Integer> currentMarkings = new HashMap<String, Integer>();
	
	/**
	 * The iteration index, for determining where to store output data
	 * */
	protected int index;

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
		//Get iteration index and increment i for next iteration.
		synchronized(iLock){
			index = i++;
		}
	}

	/**
	 * Set the input.
	 * 
	 * @param spn {@link StochasticPetriNet}.
	 * @param stoptime The maximum of the last time point of the simulation.
	 */
	public static void setInput(StochasticPetriNet spn, Parameters params){		
		Algorithm.stoptime = params.getStoptime();
		Algorithm.rateMode = params.getRateMode();
		Algorithm.threshold = params.getSimThreshold();
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
	public static void updateMarkings(Reaction reaction,
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
	public static HashMap<Integer, LinkedList<DataPoint>> getOutput() {
		synchronized(mapLock){
			return resultData;
		}
	}

	/**
	 * Set the output
	 * 
	 * @param reactionEvent See {@link DataPoint}.
	 */
	protected static void addPartialResult(DataPoint state, int index){
		synchronized(mapLock){
			if(resultData.get(index)==null){
				resultData.put(index, new LinkedList<DataPoint>());
			}
			resultData.get(index).add(state);
		}
	}

	/**
	 * Clears data from a single iteration
	 * Can be used to cleanup timeouted threads
	 * 
	 * @param reactionEvent See {@link DataPoint}.
	 */
	protected static void deletePartialResult(int index){
		synchronized(mapLock){
			if(!(resultData.get(index)==null)){
				resultData.remove(index);
			}
		}
	}
}
