package dk.dtu.sb.simulator.algorithm;

import dk.dtu.sb.data.StochasticPetriNet;

public abstract class Algorithm {
	
	public StochasticPetriNet spn;
	
	public abstract void run(double stoptime);
}
