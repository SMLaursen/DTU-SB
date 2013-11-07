package dk.dtu.sb.algorithm;

import java.util.HashMap;

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
	}
	
	/**
	 * 
	 */
	public void run() {
		throw new UnsupportedOperationException("Not implemented.");
	}
}
