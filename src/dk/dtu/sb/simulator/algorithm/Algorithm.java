package dk.dtu.sb.simulator.algorithm;

import dk.dtu.sb.data.StochasticPetriNet;

public abstract class Algorithm {
	
	protected StochasticPetriNet spn;
	
	public void setSPN(StochasticPetriNet spn) {
	    this.spn = spn;
	}
	
	public abstract void run(double stoptime);
}
