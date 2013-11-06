package dk.dtu.sb.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class OutputData {

	public LinkedList<ReactionEvent> plotData = new LinkedList<ReactionEvent>();
	public HashMap<String, Integer> initialMarkings = new HashMap<String,Integer>();
	public int iterations;
	
	public OutputData(LinkedList<ReactionEvent> plotData, Map<String, Integer> initialMarkings,int iterations) {
		this.plotData.addAll(plotData);
		this.initialMarkings.putAll(initialMarkings);
		this.iterations = iterations;
	}
	
}
