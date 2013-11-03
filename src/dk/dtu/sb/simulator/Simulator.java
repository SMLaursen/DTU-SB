package dk.dtu.sb.simulator;

import java.util.Properties;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.output.Output;
import dk.dtu.sb.simulator.algorithm.Algorithm;
import dk.dtu.sb.simulator.algorithm.GillespieAlgorithm;

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
    
    public void setSPN(StochasticPetriNet spn) {
        this.spn = spn;
    }
    
    public void setParams(Parameters params) {
        this.params = params;
    }
    
    private int getIterations() {
        int iterations;
        try {
            iterations = Integer.parseInt(this.params.getProperty(Parameters.PARAM_SIM_ITERATIONS, ""+Parameters.PARAM_SIM_ITERATIONS_DEFAULT));
        } catch (NumberFormatException e) {
            iterations = Parameters.PARAM_SIM_ITERATIONS_DEFAULT;
            Util.log.warn("ITERATION specified in the properties file was not a number.");
        }
        return iterations;
    }
    
    private double getStoptime() {
        double stoptime;
        try {
            stoptime = Double.parseDouble(this.params.getProperty(Parameters.PARAM_SIM_STOPTIME, ""+Parameters.PARAM_SIM_STOPTIME_DEFAULT));
        } catch (NumberFormatException e) {
            stoptime = Parameters.PARAM_SIM_STOPTIME_DEFAULT;
            Util.log.warn("STOPTIME specified in the properties file was not a number.");
        }
        return stoptime;
    }
    
    /**
     * Simulates using the given stoptime and no of iterations
     * @param iterations
     * @param stoptime
     */
    public void simulate() {
                
        long startTime = System.currentTimeMillis();
        
    	for (int i = 0; i < getIterations(); i++) {
    		algorithm.run(getStoptime());
    	}
    	
    	long endTime = System.currentTimeMillis();
    	
    	Util.log.debug("Simulation ended in : "+(endTime-startTime)+"ms");
    }
    
    public Output getOutput() {
        return null;
    }
}
