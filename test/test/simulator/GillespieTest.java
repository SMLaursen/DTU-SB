package test.simulator;

import org.apache.commons.logging.impl.SimpleLog;
import org.junit.Test;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
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

        p.setIterations(50);
        p.setNoOfThreads(4);
        p.setStoptime(100000);
        p.setOutStepSize(50);

        Util.log.setLevel(SimpleLog.LOG_LEVEL_INFO);
        Simulator simulator = new Simulator(parser.parse(), p);
        simulator.simulate();

//        GraphGUI graph = new GraphGUI();
//        graph.setParameters(p);
//        graph.setData(simulator.getOutputData());
//        graph.process();
    }
    
    @Test
    public void testRepressilator() {
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile("test/test/parser/BIOMD0000000012.xml");
        } catch (Exception e) {

        }

        Parameters p = new Parameters();

        p.setIterations(50);
        p.setNoOfThreads(4);
        p.setStoptime(100000);
        p.setOutStepSize(50);

        Util.log.setLevel(SimpleLog.LOG_LEVEL_INFO);
        Simulator simulator = new Simulator(parser.parse(), p);
        simulator.simulate();

        GraphGUI graph = new GraphGUI();
        graph.setParameters(p);
        graph.setData(simulator.getOutputData());
        graph.process();
    }

//    @Test
//    public void test2() {
//        SBMLParser parser = new SBMLParser();
//        try {
//            parser.readFile("test/test/simulator/neg_feedback_wo_read.xml");
//        } catch (Exception e) {
//
//        }
//
//        GillespieAlgorithm algorithm = new GillespieAlgorithm();
//        Simulator simulator = new Simulator(parser.parse());
//        simulator.simulate();
//
//        CSV csv = new CSV();
//        csv.setData(simulator.getOutputData());
//        csv.process();
//        // simulator.writeCSVFile("test/test/simulator/test_simulator.csv");
//
//    }

}
