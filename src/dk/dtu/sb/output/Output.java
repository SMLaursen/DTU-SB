package dk.dtu.sb.output;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.data.OutputData;
import dk.dtu.sb.data.Plot;
import dk.dtu.sb.data.DataPoint;

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
		//TODO : Add interpolating method!
		HashMap<String,Integer> prevMarking = new HashMap<String,Integer>();

		HashMap<String,Integer> currMarking = new HashMap<String,Integer>();
		//Count how many values in a bucket
		HashMap<String,Integer> bucketCount = new HashMap<String,Integer>();

		//Add initial marking
		currMarking.putAll(simulationData.initialMarkings);
		graphData.add(new Plot(currMarking));

		//Put values in buckets
		double bucketSize = simulationData.stopTime/params.getOutStepCount();
		for(double i = bucketSize; i < simulationData.stopTime; i+=bucketSize){
			
			// Bucket sizes reset
			for(String key : simulationData.initialMarkings.keySet()){
				bucketCount.put(key, 0);
			}
			prevMarking.clear();
			prevMarking.putAll(currMarking);
			
			currMarking.clear();
			//For each simulation set
			for(LinkedList<DataPoint> l : simulationData.data.values()){
				//Take all those values in the bucket (<i)
				while(!l.isEmpty()){
					DataPoint d=l.removeFirst();
					if(d.time > i){
						break;
					}
					for(String species : d.markings.keySet()){
						//Calculate new markings
						int prev = currMarking.containsKey(species)? currMarking.get(species) : 0;
						currMarking.put(species, prev+d.markings.get(species));
						bucketCount.put(species, bucketCount.get(species)+1);
					}
				}
				
			}
			//Store averaged intersection
			HashMap<String,Float> d = new HashMap<String, Float>();
			for(String species : getIntersection(currMarking,prevMarking)){
				d.put(species,(float) (currMarking.get(species)/bucketCount.get(species)));
			}
			graphData.add(new Plot(i,d));
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
	private HashSet<String> getIntersection(HashMap<String,Integer> mapOne, HashMap<String,Integer> mapTwo)
	{
		HashSet<String> intersection = new HashSet<String>();
		for (String key: mapOne.keySet())
		{
			if (mapOne.get(key) != (mapTwo.get(key))){
				intersection.add(key);
			}
		}
		return intersection;
	}

	/**
	 * 
	 */
	public abstract void process();
}