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
            parser.readFile("test/test/simulator/neg_feedback_wo_read.xml");
        } catch (Exception e) {

        }
        Parameters p = new Parameters();

        p.setIterations(5);
        p.setNoOfThreads(2);
        p.setStoptime(100000);
        p.setOutStepCount(0);

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
    public void testNOR() {
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile("test/test/simulator/nor.xml");
        } catch (Exception e) {

        }
        Parameters p = new Parameters();

        p.setIterations(1);
        p.setNoOfThreads(2);
        p.setStoptime(100);
        p.setOutStepCount(0);

        StochasticPetriNet spn = parser.parse();

        System.out.println(spn.toGraphviz());

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
    
    @Test
    public void testBioModelOscillator2() {
        // EmptySet is never used, maybe remove in a Compilation phase
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile("test/test/simulator/BIOMD0000000035.xml");
        } catch (Exception e) {

        }
        Parameters p = new Parameters();

        p.setIterations(2);
        p.setNoOfThreads(2);
        p.setStoptime(200);
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
    public void testBioModel_MetThr_synthesis() {
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile("test/test/simulator/BIOMD0000000068.xml");
        } catch (Exception e) {

        }
        Parameters p = new Parameters();

        p.setIterations(1);
        p.setNoOfThreads(2);
        p.setStoptime(1000);
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
    public void testBioModel_TCell_receptor_activation() {
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile("test/test/simulator/BIOMD0000000120.xml");
        } catch (Exception e) {

        }
        Parameters p = new Parameters();

        p.setIterations(5);
        p.setNoOfThreads(4);
        p.setStoptime(50000);
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
    public void testBioModel_BistableReaction() {
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile("test/test/simulator/BIOMD0000000233.xml");
        } catch (Exception e) {

        }
        Parameters p = new Parameters();

        p.setIterations(10);
        p.setNoOfThreads(2);
        p.setStoptime(2);
        p.setOutStepCount(0);

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
    public void testBioModel_SpontaneousOscillations() {
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile("test/test/simulator/BIOMD0000000099.xml");
        } catch (Exception e) {

        }
        Parameters p = new Parameters();

        p.setIterations(1);
        p.setNoOfThreads(2);
        p.setStoptime(10);
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
    public void testBioModel_NegFB_Homeostasis() {
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile("test/test/simulator/BIOMD0000000309.xml");
        } catch (Exception e) {

        }
        Parameters p = new Parameters();

        p.setIterations(2);
        p.setNoOfThreads(2);
        p.setStoptime(1000);
        p.setOutStepCount(100);

        StochasticPetriNet spn = parser.parse();

        System.out.println(spn);
        System.out.println(spn.toGraphviz());

        Util.log.setLevel(SimpleLog.LOG_LEVEL_DEBUG);
        Simulator simulator = new Simulator(spn, p);
        simulator.simulate();

        GraphGUI graph = new GraphGUI();
        graph.setParameters(p);
        graph.setData(simulator.getOutputData());
        graph.process();        
    }
    
    @Test
    public void test_Gec_Repressilator() {
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile("test/test/simulator/gec_repressilator.xml");
        } catch (Exception e) {

        }
        Parameters p = new Parameters();

        p.setIterations(2);
        p.setNoOfThreads(2);
        p.setStoptime(10000);
        p.setOutStepCount(1000);

        StochasticPetriNet spn = parser.parse();

        System.out.println(spn);
        System.out.println(spn.toGraphviz());

        Util.log.setLevel(SimpleLog.LOG_LEVEL_DEBUG);
        Simulator simulator = new Simulator(spn, p);
        simulator.simulate();

        GraphGUI graph = new GraphGUI();
        graph.setParameters(p);
        graph.setData(simulator.getOutputData());
        graph.process();        
    }
    
}
