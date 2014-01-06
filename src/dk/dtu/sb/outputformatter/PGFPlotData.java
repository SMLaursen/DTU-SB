package dk.dtu.sb.outputformatter;

import dk.dtu.sb.data.SimulationResult;

/**
 * An extension of {@link CSV} that changes the delimiter to a space, thus the
 * output will be usable by the graph LaTeX package PGFPlot.
 */
public class PGFPlotData extends CSV {

    public void process(SimulationResult data) {
        delimiter = " ";
        super.process(data);
    }

}
