package sbml;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.parser.SBMLParser;

public class SBMLParserTest {

    @Rule
    public ExpectedException thrown= ExpectedException.none();
    
    @Test
    public void testSimple() throws XMLStreamException, IOException {
        SBMLParser parser = new SBMLParser();
        
        parser.readFile("test/sbml/simple.xml");
        
        StochasticPetriNet spn = parser.parse();
        
        assertNotNull(spn.getReaction("reaction"));
        assertTrue(spn.getReaction("reaction").getProducts().containsKey("P"));
        assertTrue(spn.getReaction("reaction").getReactants().containsKey("R"));
    }
    
    @Test
    public void testNegFeed() throws XMLStreamException, IOException {
        SBMLParser parser = new SBMLParser();
        
        parser.readFile("test/sbml/neg_feedback.xml");
        
        StochasticPetriNet spn = parser.parse();
        
        
    }
    
    @Test
    public void testNegFeedWORead() throws XMLStreamException, IOException {
        SBMLParser parser = new SBMLParser();
        
        parser.readFile("test/sbml/neg_feedback_wo_read.xml");
        
        StochasticPetriNet spn = parser.parse();
        
        System.out.println(spn.toGraphviz());
        
    }

}
