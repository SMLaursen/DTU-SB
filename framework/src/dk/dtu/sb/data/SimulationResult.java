package dk.dtu.sb.data;

import java.util.Collection;
import java.util.HashMap;
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
    public SimulationResult(Collection<AlgorithmResult> algorithmResults) {
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
    public SimulationResult(Collection<AlgorithmResult> algorithmResults,
            Parameters params) {
        this.params = params;
        generatePlots(algorithmResults);
    }

    private void generatePlots(Collection<AlgorithmResult> algorithmResults) {
    	
        if (algorithmResults.size() == 0) {
            throw new RuntimeException("The input was empty.");
        } else {
            species = ((AlgorithmResult) algorithmResults.toArray()[0])
                    .getSpecies();

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
            double bucketSize = params.getSimStoptime() / params.getOutputStepCount();

            for (double time = bucketSize; time < params.getSimStoptime(); time += bucketSize) {
                // Bucket sizes reset
                bucketCount.clear();
                bucketCount.putAll(emptyBucketCount);

                // clear prevmarking
                prevMarking.clear();
                prevMarking.putAll(currMarking);

                currMarking.clear();

                // For each simulation set
                for (AlgorithmResult algorithmResult : algorithmResults) {
                    LinkedList<SimulationPoint> list = algorithmResult
                            .getSimulationPoints();
                    // for (SimulationPoint simulationPoint : algorithmResult
                    // .getSimulationPoints()) {
                    while (!list.isEmpty()) {
                        SimulationPoint simulationPoint = list.removeFirst();
                        // Take all those values in the bucket (<i)
                        if (simulationPoint.getTime() > time) {
                            break;
                        }
                        for (String species : simulationPoint.getSpecies()) {
                            // Calculate new markings
                            int prev = currMarking.containsKey(species) ? currMarking
                                    .get(species) : 0;
                            currMarking.put(species,
                                    prev + simulationPoint.getMarking(species));
                            bucketCount.put(species,
                                    bucketCount.get(species) + 1);
                        }
                    }
                }
                

                // Store averaged intersection
                HashMap<String, Float> avg = new HashMap<String, Float>();
                // TODO: determine whether the following can be removed
                /*
                 * for (String species : getDifference(currMarking,
                 * prevMarking)) { avg.put(species, (float)
                 * (currMarking.get(species) / bucketCount .get(species))); } if
                 * (avg.size() != 4) { Util.log.error("err"); }
                 */
                for (String specie : species) {
                    float marking = currMarking.containsKey(specie) ? currMarking
                            .get(specie) : 0;
                    avg.put(specie, (float) (marking / bucketCount.get(specie)));
                }
                add(time, avg);
             
            }
            //Unreference for GC
            prevMarking.clear();
            currMarking.clear();
            bucketCount.clear();
            emptyBucketCount.clear();
            prevMarking = null;
            currMarking = null;
            bucketCount = null;
            emptyBucketCount = null;
            algorithmResults.clear();
            algorithmResults = null;
            System.gc();
        }
   
    }

    // TODO: Should this be moved?
    /**
     * Get the keys with different values of the two maps.
     * 
     * @param mapOne
     * @param mapTwo
     * @return A map of those keys that have changed their values
     */
    /*private HashSet<String> getDifference(HashMap<String, Integer> mapOne,
            HashMap<String, Integer> mapTwo) {

        HashSet<String> difference = new HashSet<String>();
        for (String key : mapOne.keySet()) {
            if (!mapTwo.containsKey(key) || mapOne.get(key) != mapTwo.get(key)) {
                difference.add(key);
            }
        }
        for (String key : mapTwo.keySet()) {
            if (!mapOne.containsKey(key)) {
                difference.add(key);
            }
        }

        return difference;
    }*/

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

    /**
     * Add the given plot point to the result.
     * 
     * @param plotPoint
     *            The plot point.
     */
    public void add(PlotPoint plotPoint) {
        plotPoints.add(plotPoint);
    }

    /**
     * Add the given raw variables as a {@link PlotPoint} in
     * {@link #add(PlotPoint)}.
     * 
     * @param time
     *            A time point.
     * @param markings
     *            The markings for the time point.
     */
    public void add(double time, HashMap<String, Float> markings) {
        add(new PlotPoint(time, markings));
    }
}
