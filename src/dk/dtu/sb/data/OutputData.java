package dk.dtu.sb.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class OutputData {

	public HashMap<Integer, LinkedList<DataPoint>> data = new HashMap<Integer, LinkedList<DataPoint>>();
	public Map<String, Integer> initialMarkings;
	public int iterations;
	public double stopTime;
	
	public OutputData(HashMap<Integer, LinkedList<DataPoint>> hashMap, Map<String, Integer> map,int iterations, double stopTime) {
		data = hashMap;
		this.initialMarkings = map;
		this.iterations = iterations;
		this.stopTime = stopTime;
	}
	
}
