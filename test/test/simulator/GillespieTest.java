package test.simulator;

import org.apache.commons.logging.impl.SimpleLog;
import org.junit.Test;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.output.GraphGUI;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;

public class GillespieTest {

	    @Test
	    public void testNegFeedback() {
	        SBMLParser parser = new SBMLParser();
	        try {
	            parser.readFile("test/test/simulator/neg_feedback_wo_read.xml.xml");
	        } catch (Exception e) {
	
	        }
	        Parameters p = new Parameters();

	        p.setIterations(4);
		    p.setNoOfThreads(2);
	        p.setStoptime(1500);
	        p.setOutStepCount(1000);
	
	        StochasticPetriNet spn = parser.parse();
	        
	        System.out.println(spn);
	        
	        Util.log.setLevel(SimpleLog.LOG_LEVEL_DEBUG);
	        Simulator simulator = new Simulator(spn, p);
	        simulator.simulate();
	        
	        GraphGUI graph = new GraphGUI();
	        graph.setParameters(p);
	        graph.setData(simulator.getOutputData());
	        graph.process();
	    }

	    @Test
        public void testRepressilator() {
            SBMLParser parser = new SBMLParser();
            try {
                parser.readFile("test/test/simulator/BIOMD0000000012.xml");
            } catch (Exception e) {
    
            }
            Parameters p = new Parameters();

            p.setIterations(4);
            p.setNoOfThreads(2);
            p.setStoptime(1500);
            p.setOutStepCount(1000);
    
            StochasticPetriNet spn = parser.parse();
            
            System.out.println(spn);
            
            Util.log.setLevel(SimpleLog.LOG_LEVEL_DEBUG);
            Simulator simulator = new Simulator(spn, p);
            simulator.simulate();
            
            GraphGUI graph = new GraphGUI();
            graph.setParameters(p);
            graph.setData(simulator.getOutputData());
            graph.process();
        }
}
