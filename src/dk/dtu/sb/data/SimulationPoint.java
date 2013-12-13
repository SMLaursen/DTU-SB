package dk.dtu.sb.data;

import java.util.Map;

/**
 * 
 */
public class SimulationPoint extends DataPoint<Integer> {

    public SimulationPoint(double time, Map<String, Integer> markings) {
        super(time, markings);
    }

}
