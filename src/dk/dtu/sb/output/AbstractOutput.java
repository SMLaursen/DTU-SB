package dk.dtu.sb.output;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.data.OutputData;

/**
 *
 */
public abstract class AbstractOutput {

    protected OutputData plotData;
    protected Parameters params = new Parameters();

    /**
     * 
     * @param plotData
     */
    public void setPlotData(OutputData plotData) {
        this.plotData = plotData;
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
	 */
    public abstract void process();
}