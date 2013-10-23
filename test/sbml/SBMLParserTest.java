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
        
        StochasticPetriNet spn = parser.parseToSPN();
        
        assertNotNull(spn);
    }

}
