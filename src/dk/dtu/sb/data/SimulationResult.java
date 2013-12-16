package dk.dtu.sb.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import dk.dtu.sb.Parameters;

/**
 * This class will produce the final plots for the output of the algorithm runs.
 */
public class SimulationResult {

    private Set<String> species;
    private LinkedList<PlotPoint> plotPoints = new LinkedList<PlotPoint>();
    private Parameters params;

    /**
     * See {@link #SimulationResult(List, Parameters)}.
     */
    public SimulationResult(List<AlgorithmResult> algorithmResults) {
        this(algorithmResults, new Parameters());
    }

    /**
     * This constructor takes input from algorithm runs and produces the return
     * values for the methods {@link #getPlotPoints()} and {@link #getSpecies()}
     * .
     * 
     * @param algorithmResults
     *            A list of {@link AlgorithmResult}.
     * @param params
     *            An optional parameters object.
     */
    public SimulationResult(List<AlgorithmResult> algorithmResults,
            Parameters params) {
        this.params = params;
        generatePlots(algorithmResults);
    }

    private void generatePlots(List<AlgorithmResult> algorithmResults) {
        if (algorithmResults.size() == 0) {
            throw new RuntimeException("The input was empty.");
        } else {
            species = algorithmResults.get(0).getSpecies();

            // TODO : Add interpolating method!
            HashMap<String, Integer> prevMarking = new HashMap<String, Integer>();

            HashMap<String, Integer> currMarking = new HashMap<String, Integer>();

            // Count how many values in a bucket
            HashMap<String, Integer> bucketCount = new HashMap<String, Integer>();
            HashMap<String, Integer> emptyBucketCount = new HashMap<String, Integer>();

            for (String key : species) {
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
                for (AlgorithmResult algorithmResult : algorithmResults) {

                    LinkedList<SimulationPoint> l = algorithmResult
                            .getSimulationPoints();
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
                            currMarking.put(species, prev
                                    + dp.getMarkings().get(species));
                            bucketCount.put(species,
                                    bucketCount.get(species) + 1);
                        }
                    }
                }

                // Store averaged intersection
                HashMap<String, Float> d = new HashMap<String, Float>();
                for (String species : getDifference(currMarking, prevMarking)) {
                    d.put(species,
                            (float) (currMarking.get(species) / bucketCount
                                    .get(species)));
                }
                plotPoints.add(new PlotPoint(i, d));
            }
        }
    }

    /**
     * Get the keys with different values of the two maps.
     * 
     * @param mapOne
     * @param mapTwo
     * @return A map of those keys that have changed their values
     */
    private HashSet<String> getDifference(HashMap<String, Integer> mapOne,
            HashMap<String, Integer> mapTwo) {
        HashSet<String> difference = new HashSet<String>();
        for (String key : mapOne.keySet()) {
            if (!mapTwo.containsKey(key) || mapOne.get(key) != mapTwo.get(key)) {
                difference.add(key);
            }
        }
        return difference;
    }

    /**
     * Get all the species found in this result.
     * 
     * @return A {@link Set} of species IDs.
     */
    public Set<String> getSpecies() {
        return species;
    }

    /**
     * Get the raw plot points.
     * 
     * @return A {@link LinkedList} of {@link PlotPoint}.
     */
    public LinkedList<PlotPoint> getPlotPoints() {
        return plotPoints;
    }
}
