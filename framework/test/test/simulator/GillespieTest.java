package test.simulator;

import org.junit.Test;

import ch.qos.logback.classic.Level;
import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.outputformatter.GraphGUI;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;
import dk.dtu.sb.spn.StochasticPetriNet;

public class GillespieTest {

    @Test
    public void testNegFeedback() {
        Parameters p = new Parameters();

        p.setSimIterations(10);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(10000);
        p.setOutputStepCount(500);
        p.setSimThreshold(0.5);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/neg_feedback.xml"), p);
    }
    
    @Test
    public void testNegFeedbackReal() {
        Parameters p = new Parameters();

        p.setSimIterations(2);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(10000);
        p.setOutputStepCount(100);
        p.setSimThreshold(0.1);
        p.setSimMaxIterTime(120);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/neg_feed_real.xml"), p);
    }

    @Test
    public void testRepressilator() {
        Parameters p = new Parameters();

        p.setSimIterations(2);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(2000);
        p.setOutputStepCount(1000);
        p.setSimThreshold(1);
        //p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);
        p.saveAsFile("repressilator.properties");

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000012.xml"), p);
    }
    
    @Test
    public void testBioModelOscillator2() {
        // TODO: EmptySet is never used, maybe remove in a Compilation phase
        Parameters p = new Parameters();

        p.setSimIterations(2);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(200);
        p.setOutputStepCount(1000);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000035.xml"), p);
    }
    
    @Test
    public void testBioModel_MetThr_synthesis() {
        Parameters p = new Parameters();

        p.setSimIterations(1);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(1000);
        p.setOutputStepCount(1000);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000068.xml"), p);
    }
    
    @Test
    public void testBioModel_TCell_receptor_activation() {
        Parameters p = new Parameters();

        p.setSimIterations(5);
        p.setSimNoOfThreads(4);
        p.setSimStoptime(50000);
        p.setOutputStepCount(1000);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000120.xml"), p);     
    }
    
    @Test
    public void testBioModel_BistableReaction() {
        Parameters p = new Parameters();

        p.setSimIterations(10);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(2);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000233.xml"), p);       
    }
    
    @Test
    public void testBioModel_SpontaneousOscillations() {
        Parameters p = new Parameters();

        p.setSimIterations(1);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(10);
        p.setOutputStepCount(1000);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000099.xml"), p);       
    }
    
    @Test
    public void testBioModel_NegFB_Homeostasis() {
        Parameters p = new Parameters();

        p.setSimIterations(2);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(1000);
        p.setOutputStepCount(100);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/BIOMD0000000309.xml"), p);       
    }
    
    @Test
    public void test_Gec_Repressilator() {
        Parameters p = new Parameters();

        p.setSimIterations(1);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(100);
        p.setOutputStepCount(1000);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

        simulateAndOutput(parse("test/test/simulator/gec_repressilator.xml"), p);      
    }
    @Test
    public void testPredatorPrey() {
        Parameters p = new Parameters();

        p.setSimIterations(1);
//        p.setSimThreshold(0.0005);
        p.setSimMaxIterTime(20);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(5);
//        p.setOutStepCount(500);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CONSTANT);

        simulateAndOutput(parse("test/test/simulator/PP.xml"), p);
    }
    
    @Test
    public void test_Min_Feedback() {
        Parameters p = new Parameters();

        p.setSimIterations(50);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(1000);
        p.setOutputStepCount(10000);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);

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
        graph.process(simulator.getOutput(), p);  
    }
    
   
    
}
