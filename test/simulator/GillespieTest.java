package simulator;

import org.junit.Test;

import dk.dtu.sb.compiler.Compiler;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;
import dk.dtu.sb.simulator.algorithm.GillespieAlgorithm;
import static org.junit.Assert.*;

public class GillespieTest {
	
	@Test
	public void test1(){
		try {
	        SBMLParser parser = new SBMLParser();
	        parser.readFile("test/simulator/simple.xml");
	
	        Compiler compiler = new Compiler(parser.parse());
	        System.out.println(compiler.compile());
	        
	        GillespieAlgorithm algorithm = new GillespieAlgorithm();
	        Simulator simulator = new Simulator(compiler.compile(), algorithm);
	        simulator.simulate(1, 20);    
	    } catch (Exception e) {
	    	System.out.println(e);
	    	assertTrue(false);
	    }       
	}

}
