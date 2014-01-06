package dk.dtu.sb.data;

import java.util.Map;

import dk.dtu.sb.outputformatter.AbstractOutputFormatter;

/**
 * This class holds plots (concentrations) for time points of the final
 * simulation. Typically this will be used in the
 * {@link AbstractOutputFormatter#process(SimulationResult)} method.
 */
public class PlotPoint extends DataPoint<Float> {

    public PlotPoint(double time, Map<String, Float> markings) {
        super(time, markings);
    }

}
