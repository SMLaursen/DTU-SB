package dk.dtu.sb.output.data;

import java.util.Map;

public class PlotPoint extends DataPoint<Float> {

    public PlotPoint(double time, Map<String, Float> markings) {
        super(time, markings);
    }

}
