package dk.dtu.sb.simulator.algorithm;

import java.util.LinkedList;

import dk.dtu.sb.data.Plot;
import dk.dtu.sb.data.StochasticPetriNet;

public abstract class Algorithm {
	
	protected StochasticPetriNet spn;
	protected LinkedList<Plot> resultData;
	
	public void setSPN(StochasticPetriNet spn) {
	    this.spn = spn;
	    resultData = new LinkedList<Plot>();
	}
	
	public abstract void run(double stoptime);
	
	public LinkedList<Plot> getPlotData(){
		return resultData;
	}
}
