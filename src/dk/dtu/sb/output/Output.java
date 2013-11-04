package dk.dtu.sb.output;

import java.util.LinkedList;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.data.Plot;

/**
 *
 */
public abstract class Output {
    LinkedList<Plot> data = new LinkedList<Plot>();
    Parameters params = new Parameters();
        
    /**
     * 
     * @param data
     */
    public void setData(LinkedList<Plot> data) {
        this.data = data;
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
