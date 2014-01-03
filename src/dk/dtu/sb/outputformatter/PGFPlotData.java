package dk.dtu.sb.outputformatter;

import dk.dtu.sb.data.SimulationResult;

public class PGFPlotData extends CSV {

    public void process(SimulationResult data) {
        delimiter = " ";
        super.process(data);
    }

}
