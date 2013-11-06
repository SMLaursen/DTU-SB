package dk.dtu.sb.output;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.OutputData;
import dk.dtu.sb.data.Plot;
import dk.dtu.sb.data.ReactionEvent;

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
    public void setData(OutputData o) {
        
    	//Sort on times
    	Collections.sort(o.plotData, new Comparator<ReactionEvent>() {
            @Override
            public int compare(ReactionEvent r1, ReactionEvent r2) {
                return Double.compare(r1.time, r2.time);
            }
        });
             
        HashMap<String,Integer> marking = new HashMap<String,Integer>();
        
        // Multiply the initial marking with noOfIterations
        for(String s : o.initialMarkings.keySet()){
        	marking.put(s, o.initialMarkings.get(s)*o.iterations);
        }
    
        data.add(new Plot(0,marking));
        
        //TODO : very inefficient
        
        // Create PlotData from the OutputData
        for(ReactionEvent re : o.plotData){
        	Util.updateMarkings(re.reaction, marking);
        	data.add(new Plot(re.time,marking));
        }
        
        // Divide by no of iterations
        for(Plot p : data){
        	for(String s : p.markings.keySet()){
        		int old = p.markings.get(s);
        		p.markings.put(s, old/o.iterations);
        	}
        }
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
