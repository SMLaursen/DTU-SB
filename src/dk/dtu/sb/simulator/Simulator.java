package dk.dtu.sb.simulator;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.algorithm.Algorithm;
import dk.dtu.sb.algorithm.GillespieAlgorithm;
import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.output.Output;

public class Simulator {
    
	Algorithm algorithm;
	StochasticPetriNet spn;
	Parameters params;
	
	/**
	 * 
	 * @param spn
	 */
	public Simulator(StochasticPetriNet spn) {
        this.spn = spn;
        this.algorithm = new GillespieAlgorithm();
        this.algorithm.setSPN(this.spn);
        this.params = new Parameters();
    }
	
	/**
	 * 
	 * @param spn
	 * @param algorithm
	 */
    public Simulator(StochasticPetriNet spn, Algorithm algorithm) {
    	this.spn = spn;
    	this.algorithm = algorithm;
    	this.algorithm.setSPN(this.spn);
    	this.params = new Parameters();
    }
    /**
     * 
     * @param spn
     * @param algorithm
     * @param params
     */
    public Simulator(StochasticPetriNet spn, Algorithm algorithm, Parameters params) {
        this.spn = spn;
        this.algorithm = algorithm;
        this.algorithm.setSPN(this.spn);
        this.params = params;
    }
    
    /**
     * 
     * @param spn
     */
    public void setSPN(StochasticPetriNet spn) {
        this.spn = spn;
    }
    
    /**
     * 
     * @param params
     */
    public void setParams(Parameters params) {
        this.params = params;
    }
    
    /**
     * Simulates using the given stoptime and no of iterations
     * @param iterations
     * @param stoptime
     */
    public void simulate() {
                
        long startTime = System.currentTimeMillis();
        
    	for (int i = 0; i < params.getIterations(); i++) {
    		algorithm.run(params.getStoptime());
    	}
    	
    	long endTime = System.currentTimeMillis();
    	
    	Util.log.debug("Simulation ended in : "+(endTime-startTime)+"ms");
    }
    
    /**
     * 
     * @return
     */
    public Output getOutput() {
        return null;
    }
}
