package test.simulator;

import org.apache.commons.logging.impl.SimpleLog;
import org.junit.Test;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.output.CSV;
import dk.dtu.sb.output.GraphGUI;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;

public class GillespieTest {

	    @Test
	    public void testNegFeedback() {
	        SBMLParser parser = new SBMLParser();
	        try {
	            parser.readFile("test/test/simulator/neg_feedback_wo_read.xml");
	        } catch (Exception e) {
	
	        }
	        Parameters p = new Parameters();
	        p.setIterations(20);
		    p.setNoOfThreads(4);
	        p.setStoptime(100000);
	        p.setOutStepSize(1);
	
	        //Util.log.setLevel(SimpleLog.LOG_LEVEL_DEBUG);
	        StochasticPetriNet spn = parser.parse();
	        System.out.println(spn);
	        Simulator simulator = new Simulator(spn, p);
	        simulator.simulate();
	
//	        GraphGUI graph = new GraphGUI();
//	        graph.setParameters(p);
//	        graph.setData(simulator.getOutputData());
//	        graph.process();
	    }
	    
	    @Test
        public void testNegFeedbackSuite() {
            SBMLParser parser = new SBMLParser();
            try {
                parser.readFile("test/test/simulator/neg_feedback_wo_read.xml");
            } catch (Exception e) {
    
            }
            Parameters p = new Parameters();
            p.setIterations(20);
            p.setNoOfThreads(4);
            p.setStoptime(100000);
            p.setOutStepSize(1);
    
            //Util.log.setLevel(SimpleLog.LOG_LEVEL_DEBUG);
            StochasticPetriNet spn = parser.parse();
    
            long totalTime = 0;
            int TIMES = 20;
            
            for (int i = 0; i < TIMES; i++) {
                Simulator simulator = new Simulator(spn, p);
                simulator.simulate();
                totalTime += simulator.getSimulationTime();
            }
            
            System.out.println("Avg: " + totalTime / TIMES);
        }

	    @Test
        public void testRepressilator() {
            SBMLParser parser = new SBMLParser();
            try {
                parser.readFile("test/test/simulator/BIOMD0000000012.xml");
            } catch (Exception e) {
    
            }
            Parameters p = new Parameters();
            p.setIterations(1);
            p.setNoOfThreads(4);
            p.setStoptime(100000);
            p.setOutStepSize(1);
    
            //Util.log.setLevel(SimpleLog.LOG_LEVEL_DEBUG);
            StochasticPetriNet spn = parser.parse();
            System.out.println(spn);
            Simulator simulator = new Simulator(spn, p);
            simulator.simulate();
    
            GraphGUI graph = new GraphGUI();
            graph.setParameters(p);
            graph.setData(simulator.getOutputData());
            graph.process();
        }


}
