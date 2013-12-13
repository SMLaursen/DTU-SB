package dk.dtu.sb.output;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.output.data.DataPoint;
import dk.dtu.sb.output.data.PlotPoint;
import dk.dtu.sb.output.data.SimulationPoint;

/**
 *
 */
public abstract class AbstractOutput {

    protected LinkedList<PlotPoint> graphData = new LinkedList<PlotPoint>();
    protected Parameters params = new Parameters();

    public void setData(HashMap<Integer, LinkedList<SimulationPoint>> simulationData) {
        // TODO : Add interpolating method!
        HashMap<String, Integer> prevMarking = new HashMap<String, Integer>();

        HashMap<String, Integer> currMarking = new HashMap<String, Integer>();
        
        // Count how many values in a bucket
        HashMap<String, Integer> bucketCount = new HashMap<String, Integer>();
        HashMap<String, Integer> emptyBucketCount = new HashMap<String, Integer>();

        // Add initial marking
        //currMarking.putAll(simulationData.initialMarkings);
        //graphData.add(0, new DataPoint(currMarking));
        
        for (String key : simulationData.get(0).get(0).getMarkings().keySet()) {
            emptyBucketCount.put(key, 0);
        }

        // Put values in buckets
        double bucketSize = params.getStoptime() / params.getOutStepCount();
        
        for (double i = bucketSize; i < params.getStoptime(); i += bucketSize) {
            // Bucket sizes reset
            bucketCount.clear();
            bucketCount.putAll(emptyBucketCount);
            
            // clear prevmarking
            prevMarking.clear();
            prevMarking.putAll(currMarking);

            currMarking.clear();
            
            // For each simulation set
            for (LinkedList<SimulationPoint> l : simulationData.values()) {
                // Take all those values in the bucket (<i)
                while (!l.isEmpty()) {
                    DataPoint<Integer> dp = l.removeFirst();
                    if (dp.getTime() > i) {
                        break;
                    }
                    for (String species : dp.getMarkings().keySet()) {
                        // Calculate new markings
                        int prev = currMarking.containsKey(species) ? currMarking
                                .get(species) : 0;
                        currMarking.put(species,
                                prev + dp.getMarkings().get(species));
                        bucketCount.put(species, bucketCount.get(species) + 1);
                    }
                }

            }
            // Store averaged intersection
            HashMap<String, Float> d = new HashMap<String, Float>();
            for (String species : getIntersection(currMarking, prevMarking)) {
                d.put(species, (float) (currMarking.get(species) / bucketCount
                        .get(species)));
            }
            graphData.add(new PlotPoint(i, d));
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
    private HashSet<String> getIntersection(HashMap<String, Integer> mapOne,
            HashMap<String, Integer> mapTwo) {
        HashSet<String> intersection = new HashSet<String>();
        for (String key : mapOne.keySet()) {
            if (mapOne.get(key) != (mapTwo.get(key))) {
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