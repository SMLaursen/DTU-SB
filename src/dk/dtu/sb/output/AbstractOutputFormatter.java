package dk.dtu.sb.output;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.data.SimulationResult;

/**
 * Implementations of output formatters should extend this class.
 * {@link #process(SimulationResult)} is the only method that needs to be
 * implemented. the method {@link #process(SimulationResult, Parameters)} will
 * automatically call {@link #process(SimulationResult)}, and {@link #params}
 * will be available in this method.
 */
public abstract class AbstractOutputFormatter {

    /**
     * The parameters object available in output formatter implementations. Use
     * {@link #process(SimulationResult, Parameters)} to override the default
     * object.
     */
    protected Parameters params = new Parameters();

    /**
     * This method should process the input data, e.g. by showing a graph or by
     * outputting in a standardised format. This is the only method that output
     * formatters need to implement.
     * 
     * @param data
     *            The result from the simulation.
     */
    public abstract void process(SimulationResult data);

    /**
     * This method can be used if parameters from the parameters object should
     * be used.
     * 
     * @param data
     *            The result from the simulation.
     * @param params
     *            The concrete parameters object.
     */
    public void process(SimulationResult data, Parameters params) {
        this.params = params;
        process(data);
    }
}