package dk.dtu.sb.output.data;

import java.util.HashMap;

import dk.dtu.sb.spn.Reaction;

/**
 * 
 */
public class DataPoint {

    /**
     * 
     */
	public double time;
	
	/**
	 * 
	 */
	public HashMap<String,Integer> markings = new HashMap<String,Integer>();
	
	/**
	 * Default constructor.
	 * 
	 * @param time
	 * @param reaction See {@link Reaction}.
	 */
	public DataPoint(double time, HashMap<String, Integer> currMarking){
		this.time = time;		
		this.markings.putAll(currMarking);
	}
	
	public String toString(){
		return "[" + time + "] : " + markings;
	}
}
