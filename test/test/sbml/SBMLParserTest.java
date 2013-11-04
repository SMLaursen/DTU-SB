package test.sbml;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.parser.SBMLParser;

public class SBMLParserTest {
    
    @Test
    public void testSimple() throws Exception {
        SBMLParser parser = new SBMLParser();
        
        parser.readFile("test/test/sbml/simple.xml");
        
        StochasticPetriNet spn = parser.parse();
        
        assertNotNull(spn.getReaction("reaction"));
        assertTrue(spn.getReaction("reaction").getProducts().containsKey("P"));
        assertTrue(spn.getReaction("reaction").getReactants().containsKey("R"));
    }
    
    @Test(expected=FileNotFoundException.class)
    public void testNoFile() throws Exception {
        SBMLParser parser = new SBMLParser();
        parser.readFile("test/test/sbml/fail.xml");
    }
    
    @Test
    public void testNegFeed() throws Exception {
        SBMLParser parser = new SBMLParser();
        
        parser.readFile("test/test/sbml/neg_feedback.xml");
        
        StochasticPetriNet spn = parser.parse();
    }
    
    @Test
    public void testNegFeedWORead() throws Exception {
        SBMLParser parser = new SBMLParser();
        
        parser.readFile("test/test/sbml/neg_feedback_wo_read.xml");
        
        StochasticPetriNet spn = parser.parse();
        
        System.out.println(spn.toGraphviz());
        
    }

}
