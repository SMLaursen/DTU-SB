package test.simulator;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import dk.dtu.sb.algorithm.GillespieAlgorithm;
import dk.dtu.sb.compiler.Compiler;
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

        Compiler compiler = new Compiler(parser.parse());
        System.out.println(compiler.compile());
        
        GillespieAlgorithm algorithm = new GillespieAlgorithm();
        Simulator simulator = new Simulator(compiler.compile(), algorithm);
        simulator.simulate();
	}

}
