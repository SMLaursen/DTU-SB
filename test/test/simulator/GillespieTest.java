package test.simulator;

import org.junit.Test;

import ch.qos.logback.classic.Level;
import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.output.GraphGUI;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;
import dk.dtu.sb.spn.StochasticPetriNet;

public class GillespieTest {

    @Test
    public void testNegFeedback() {
        Parameters p = new Parameters();

        p.setIterations(10);
        p.setNoOfThreads(4);
        p.setStoptime(10000);
        p.setOutStepCount(1000);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/neg_feedback.xml"), p);
    }
    
    @Test
    public void testNegFeedbackReal() {
        Parameters p = new Parameters();

        p.setIterations(2);
        p.setNoOfThreads(2);
        p.setStoptime(10000);
        p.setOutStepCount(100);
        p.setSimThreshold(0.1);
        p.setMaxIterTime(120);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/neg_feed_real.xml"), p);
    }

    @Test
    public void testRepressilator() {
        Parameters p = new Parameters();

        p.setIterations(2);
        p.setNoOfThreads(2);
        p.setStoptime(6000);
        p.setOutStepCount(1000);
        p.setSimThreshold(1);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000012.xml"), p);
    }
    
    @Test
    public void testBioModelOscillator2() {
        // TODO: EmptySet is never used, maybe remove in a Compilation phase
        Parameters p = new Parameters();

        p.setIterations(2);
        p.setNoOfThreads(2);
        p.setStoptime(200);
        p.setOutStepCount(1000);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000035.xml"), p);
    }
    
    @Test
    public void testBioModel_MetThr_synthesis() {
        Parameters p = new Parameters();

        p.setIterations(1);
        p.setNoOfThreads(2);
        p.setStoptime(1000);
        p.setOutStepCount(1000);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000068.xml"), p);
    }
    
    @Test
    public void testBioModel_TCell_receptor_activation() {
        Parameters p = new Parameters();

        p.setIterations(5);
        p.setNoOfThreads(4);
        p.setStoptime(50000);
        p.setOutStepCount(1000);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000120.xml"), p);     
    }
    
    @Test
    public void testBioModel_BistableReaction() {
        Parameters p = new Parameters();

        p.setIterations(10);
        p.setNoOfThreads(2);
        p.setStoptime(2);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000233.xml"), p);       
    }
    
    @Test
    public void testBioModel_SpontaneousOscillations() {
        Parameters p = new Parameters();

        p.setIterations(1);
        p.setNoOfThreads(2);
        p.setStoptime(10);
        p.setOutStepCount(1000);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000099.xml"), p);       
    }
    
    @Test
    public void testBioModel_NegFB_Homeostasis() {
        Parameters p = new Parameters();

        p.setIterations(2);
        p.setNoOfThreads(2);
        p.setStoptime(1000);
        p.setOutStepCount(100);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000309.xml"), p);       
    }
    
    @Test
    public void test_Gec_Repressilator() {
        Parameters p = new Parameters();

        p.setIterations(1);
        p.setNoOfThreads(2);
        p.setStoptime(100);
        p.setOutStepCount(1000);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/gec_repressilator.xml"), p);      
    }
    @Test
    public void testPredatorPrey() {
        Parameters p = new Parameters();

        p.setIterations(1);
//        p.setSimThreshold(0.0005);
        p.setMaxIterTime(20);
        p.setNoOfThreads(2);
        p.setStoptime(5);
//        p.setOutStepCount(500);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CONSTANT);

        simulateAndOutput(parse("test/test/simulator/PP.xml"), p);
    }
    
    @Test
    public void test_Min_Feedback() {
        Parameters p = new Parameters();

        p.setIterations(50);
        p.setNoOfThreads(2);
        p.setStoptime(1000);
        p.setOutStepCount(10000);
        p.setRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000325.xml"), p);      
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
    
    public void simulateAndOutput(StochasticPetriNet spn, Parameters p) {
        System.out.println(spn.toGraphviz());
        
        Util.log.setLevel(Level.DEBUG);
        Simulator simulator = new Simulator(spn, p);
        simulator.simulate();

        GraphGUI graph = new GraphGUI();
        graph.setParameters(p);
        graph.setData(simulator.getOutput());
        graph.process();  
    }
    
   
    
}
