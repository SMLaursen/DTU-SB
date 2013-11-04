package dk.dtu.sb.data;

import java.util.HashMap;

/**
 * 
 */
public class Plot {
    
	public double time;
	public HashMap<String,Integer> markings = new HashMap<String,Integer>();
	
	public Plot(double time, HashMap<String,Integer> currentMarkings){
		this.time = time;		
		markings.putAll(currentMarkings);
	}
	
	public String toString(){
		return "[" + time + "] : " + markings;
	}
}
