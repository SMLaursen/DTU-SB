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
    
	protected LinkedList<Plot> data = new LinkedList<Plot>();
	protected Parameters params = new Parameters();

	/**
	 * 
	 * @param data
	 */
	public void setData(OutputData outputData) {

		//Sort on times
		Collections.sort(outputData.plotData, new Comparator<ReactionEvent>() {
			@Override
			public int compare(ReactionEvent r1, ReactionEvent r2) {
				return Double.compare(r1.time, r2.time);
			}
		});

		HashMap<String,Integer> currMarking = new HashMap<String,Integer>();
		
		// Multiply the initial marking with noOfIterations
		for(String key : outputData.initialMarkings.keySet()){
			currMarking.put(key, outputData.initialMarkings.get(key)*outputData.iterations);
		}
		//Add initial marking
		data.add(new Plot(0,currMarking));
		
		// Create PlotData from the OutputData
		int i = 1;
		HashMap<String,Integer> prevMarking = new HashMap<String,Integer>();
		for(ReactionEvent reaction : outputData.plotData){
			
			if(i % params.getOutStepSize() == 0){
				prevMarking.clear();
				prevMarking.putAll(currMarking);
			}
			
			Algorithm.updateMarkings(reaction.reaction, currMarking);		
			
			//Enforce stepsize
			if(i % params.getOutStepSize() == 0){
				data.add(new Plot(reaction.time,getIntersection(currMarking,prevMarking)));
			}
			i++;
		}
		
		// Divide by no of iteration
		for(Plot p : data){
			for(String s : p.markings.keySet()){
				//Update value
				p.markings.put(s, p.markings.get(s)/outputData.iterations);
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

