package test.outputformatter;

import java.io.File;

import org.junit.Test;
import static org.junit.Assert.*;

import ch.qos.logback.classic.Level;
import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.SimulationResult;
import dk.dtu.sb.outputformatter.GraphGUI;
import dk.dtu.sb.outputformatter.PGFPlotData;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;
import dk.dtu.sb.spn.StochasticPetriNet;

public class PGFPlotDataTest {

    @Test
    public void testNegFeedback10Iterations() {
        Util.log.setLevel(Level.DEBUG);
        
        Parameters p = new Parameters();

        p.setIterations(10);
        p.setNoOfThreads(2);
        p.setStoptime(6000);
        p.setOutStepCount(1000);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);
        p.setOutputFilename("neg_feedback_10.dat");
        
        File file = new File(p.getOutputFilename());
        
        if (file.exists()) {
            file.delete();
        }
        
        assertFalse(file.exists());

        Simulator simulator = new Simulator(parse("test/test/simulator/neg_feedback.xml"), p);
        simulator.simulate();

        SimulationResult result = simulator.getOutput();
        
        GraphGUI graph = new GraphGUI();
        graph.process(result, p);
        
        PGFPlotData plot = new PGFPlotData();
        plot.process(result, p);
        
        assertTrue(file.exists());
    }
    
    @Test
    public void testNegFeedback1Iteration() {
        Util.log.setLevel(Level.DEBUG);
        
        Parameters p = new Parameters();

        p.setIterations(1);
        p.setNoOfThreads(2);
        p.setStoptime(6000);
        p.setOutStepCount(1000);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);
        p.setOutputFilename("neg_feedback_1.dat");
        
        File file = new File(p.getOutputFilename());
        
        if (file.exists()) {
            file.delete();
        }
        
        assertFalse(file.exists());

        Simulator simulator = new Simulator(parse("test/test/simulator/neg_feedback.xml"), p);
        simulator.simulate();

        SimulationResult result = simulator.getOutput();
        
        GraphGUI graph = new GraphGUI();
        graph.process(result, p);
        
        PGFPlotData plot = new PGFPlotData();
        plot.process(result, p);
        
        assertTrue(file.exists());
    }
    
    public StochasticPetriNet parse(String filename) {
        StochasticPetriNet spn = null;
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile(filename);
            spn = parser.parse();
        } catch (Exception e) {
            System.err.println("ERROR WHEN PARSING");
        }
        return spn;
    }
    
   
    
}
