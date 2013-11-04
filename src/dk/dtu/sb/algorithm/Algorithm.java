package dk.dtu.sb.algorithm;

import java.util.LinkedList;

import dk.dtu.sb.data.Plot;
import dk.dtu.sb.data.StochasticPetriNet;

public abstract class Algorithm {
	
	protected StochasticPetriNet spn;
	protected LinkedList<Plot> resultData = new LinkedList<Plot>();
	
	public void setSPN(StochasticPetriNet spn) {
	    this.spn = spn;
	}
	
	public abstract void run(double stoptime);
	
	public LinkedList<Plot> getPlotData(){
		return resultData;
	}
}
