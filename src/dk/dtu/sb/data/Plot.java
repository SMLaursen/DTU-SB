package dk.dtu.sb.data;

import java.util.HashMap;

/**
 * 
 */
public class Plot {
    
	public double time;
	public HashMap<String,Float> markings = new HashMap<String,Float>();
	
	public Plot(double time, HashMap<String, Integer> currMarking){
		this.time = time;		
		//Store as float values
		for(String key : currMarking.keySet()){
			markings.put(key, (float) currMarking.get(key));
		}
	}
	
	public String toString(){
		return "[" + time + "] : " + markings;
	}
}
