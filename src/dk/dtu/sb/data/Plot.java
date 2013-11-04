package dk.dtu.sb.data;

import java.util.HashMap;

public class Plot {
	public double time;
	public HashMap<String,Integer> markings;
	
	public Plot(Double time, HashMap<String,Integer> currentMarkings){
		this.time = time;
		markings = new HashMap<String,Integer>();
		markings.putAll(currentMarkings);
	}
	
	public String toString(){
		return "["+time+"]  :  "+markings;
	}
}
