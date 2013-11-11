package dk.dtu.sb.output;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.algorithm.Algorithm;
import dk.dtu.sb.data.OutputData;
import dk.dtu.sb.data.Plot;
import dk.dtu.sb.data.ReactionEvent;

/**
 *
 */
public abstract class Output {
	protected LinkedList<Plot> graphData = new LinkedList<Plot>();
	protected Parameters params = new Parameters();

	/**
	 * 
	 * @param tempOutputData
	 */
	public void setData(OutputData simulationData) {

		//Sort simulationData on times
		Collections.sort(simulationData.data, new Comparator<ReactionEvent>() {
			@Override
			public int compare(ReactionEvent r1, ReactionEvent r2) {
				return Double.compare(r1.time, r2.time);
			}
		});
		
		//Calculate the stepsize of data to include in output
		int stepsize = 1;
		if(params.getOutStepCount() > 0){
			stepsize = simulationData.data.size() / params.getOutStepCount();
		}

		HashMap<String,Integer> currMarking = new HashMap<String,Integer>();
		
		// Multiply the initial marking with noOfIterations
		for(String key : simulationData.initialMarkings.keySet()){
			currMarking.put(key, simulationData.initialMarkings.get(key)*simulationData.iterations);
		}
		//Add initial marking
		graphData.add(new Plot(0,currMarking));
		
		// Create PlotData from the OutputData
		int i = 1;
		HashMap<String,Integer> prevMarking = new HashMap<String,Integer>();
		for(ReactionEvent reaction : simulationData.data){
			
			if(i % stepsize == 0){
				prevMarking.clear();
				prevMarking.putAll(currMarking);
			}
			
			Algorithm.updateMarkings(reaction.reaction, currMarking);		
			
			//Enforce stepsize
			if(i % stepsize == 0){
				graphData.add(new Plot(reaction.time,getIntersection(currMarking,prevMarking)));
			}
			i++;
		}
		//Add last output point to ensure it will not get cut-off.
		graphData.add(new Plot(simulationData.stopTime,currMarking));
		
		// Divide by no of iteration and put result in graphData
		for(Plot p : graphData){
			for(String s : p.markings.keySet()){
				p.markings.put(s, p.markings.get(s) / simulationData.iterations);
			}
	
		}
	}

	/**
	 * 
	 * @param params
	 */
	public void setParameters(Parameters params) {
		this.params = params;
	}
	
	/**
	 * 
	 * @param mapOne
	 * @param mapTwo
	 * @return A map of those keys that have changed their values
	 */
	private HashMap<String,Integer> getIntersection(HashMap<String,Integer> mapOne, HashMap<String,Integer> mapTwo)
	{
		HashMap<String,Integer> intersection = new HashMap<String,Integer>();
	    for (String key: mapOne.keySet())
	    {
	        if (mapOne.get(key) != (mapTwo.get(key))){
	           intersection.put(key, mapOne.get(key));
	        }
	    }
	    return intersection;
	}

	/**
	 * 
	 */
	public abstract void process();
}