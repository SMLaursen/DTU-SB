package dk.dtu.sb.output;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.data.SimulationResult;

/**
 *
 */
public abstract class AbstractOutput {
    
    protected Parameters params = new Parameters();

    /**
     * 
     * @param data
     */
    public abstract void process(SimulationResult data);
    
    /**
     * 
     * @param data
     * @param params
     */
    public void process(SimulationResult data, Parameters params) {
        this.params = params;
        process(data);
    }
}