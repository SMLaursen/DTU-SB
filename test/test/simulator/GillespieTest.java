package test.simulator;

import org.junit.Test;

import dk.dtu.sb.algorithm.GillespieAlgorithm;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;
import static org.junit.Assert.*;

public class GillespieTest {
	
	@Test
	public void test1(){
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile("test/test/simulator/neg_feedback_wo_read.xml");
        } catch (Exception e) {

        }
        
        GillespieAlgorithm algorithm = new GillespieAlgorithm();
        Simulator simulator = new Simulator(parser.parse(), algorithm);
        simulator.simulate();
        simulator.displayResultGUI();        
	}
	
	@Test
	public void test2(){
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile("test/test/simulator/neg_feedback_wo_read.xml");
        } catch (Exception e) {

        }
        
        GillespieAlgorithm algorithm = new GillespieAlgorithm();
        Simulator simulator = new Simulator(parser.parse(), algorithm);
        simulator.simulate();
        simulator.writeCSVFile("test/test/simulator/test_simulator.csv"); 
           
	}

}
