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
    
	LinkedList<Plot> data = new LinkedList<Plot>();
	Parameters params = new Parameters();

	/**
	 * 
	 * @param data
	 */
	public void setData(OutputData o) {

		//Sort on times
		Collections.sort(o.plotData, new Comparator<ReactionEvent>() {
			@Override
			public int compare(ReactionEvent r1, ReactionEvent r2) {
				return Double.compare(r1.time, r2.time);
			}
		});

		HashMap<String,Integer> marking = new HashMap<String,Integer>();
		
		// Multiply the initial marking with noOfIterations
		for(String s : o.initialMarkings.keySet()){
			marking.put(s, o.initialMarkings.get(s)*o.iterations);
		}
		data.add(new Plot(0,marking));
		
		// Create PlotData from the OutputData
		int i = 1;
		HashMap<String,Integer> old = new HashMap<String,Integer>();
		for(ReactionEvent re : o.plotData){
			
			if(i % params.getOutStepSize() == 0){
				old.clear();
				old.putAll(marking);
			}
			
			Algorithm.updateMarkings(re.reaction, marking);		
			
			//Enforce stepsize
			if(i % params.getOutStepSize() == 0){
				data.add(new Plot(re.time,getIntersection(marking,old)));
			}
			i++;
		}
		
		// Divide by no of iteration
		for(Plot p : data){
			for(String s : p.markings.keySet()){
				//Update value
				p.markings.put(s, p.markings.get(s)/o.iterations);
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
	 * @return
	 */
	private HashMap<String,Integer> getIntersection(HashMap<String,Integer> mapOne, HashMap<String,Integer> mapTwo)
	{
		HashMap<String,Integer> intersection = new HashMap<String,Integer>();
	    for (String key: mapOne.keySet())
	    {
	        if (mapOne.get(key)==mapTwo.get(key)){
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

