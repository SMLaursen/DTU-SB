package dk.dtu.sb.data;

import java.util.Map;

import dk.dtu.sb.algorithm.Algorithm;

/**
 * This class holds markings for time points for one simulation, i.e. . Typically this
 * will be used in the {@link Algorithm#run()} method.
 */
public class SimulationPoint extends DataPoint<Integer> {

    public SimulationPoint(double time, Map<String, Integer> markings) {
        super(time, markings);
    }

}
