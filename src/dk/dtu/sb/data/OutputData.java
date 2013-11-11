package dk.dtu.sb.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class OutputData {

	public LinkedList<ReactionEvent> data = new LinkedList<ReactionEvent>();
	public HashMap<String, Integer> initialMarkings = new HashMap<String,Integer>();
	public int iterations;
	public double stopTime;
	
	public OutputData(LinkedList<ReactionEvent> plotData, Map<String, Integer> initialMarkings,int iterations, double stopTime) {
		this.data.addAll(plotData);
		this.initialMarkings.putAll(initialMarkings);
		this.iterations = iterations;
		this.stopTime = stopTime;
	}
	
}
