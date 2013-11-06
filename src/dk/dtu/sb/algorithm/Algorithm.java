package dk.dtu.sb.algorithm;

import java.util.LinkedList;

import dk.dtu.sb.data.Plot;
import dk.dtu.sb.data.ReactionEvent;
import dk.dtu.sb.data.StochasticPetriNet;

public abstract class Algorithm {
	
	protected StochasticPetriNet spn;
	protected LinkedList<ReactionEvent> resultData = new LinkedList<ReactionEvent>();
	
	public void setSPN(StochasticPetriNet spn) {
	    this.spn = spn;
	}
	
	public abstract void run(double stoptime);
	
	public LinkedList<ReactionEvent> getPlotData(){
		return resultData;
	}
}
