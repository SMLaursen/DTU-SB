package dk.dtu.sb.simulator;

import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.output.Output;
import dk.dtu.sb.simulator.algorithm.Algorithm;

public class Simulator {

	Algorithm algorithm;
	StochasticPetriNet spn;
	
    public Simulator(StochasticPetriNet spn, Algorithm algorithm) {
    	this.algorithm = algorithm;
    	this.algorithm.spn = spn;
    	
    }
    /**Simulates using the given stoptime and no of iterations*/
    public void simulate(int iterations, double stoptime) {
    	long startTime = System.currentTimeMillis();
    	for(int i=0; i<iterations;i++){
    		algorithm.run(stoptime);
    	}
    	long endTime = System.currentTimeMillis();
    	System.out.println("Simulation ended in : "+(endTime-startTime)+"ms");
    }
    
    public Output getOutput() {
        return null;
    }
}
