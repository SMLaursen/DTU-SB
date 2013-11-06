package test.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.parser.SBMLParser;

public class SBMLParserTest {
    
    @Test
    public void testSimple() throws Exception {
        SBMLParser parser = new SBMLParser();
        
        parser.readFile("test/test/parser/simple.xml");
        
        StochasticPetriNet spn = parser.parse();
        
        assertNotNull(spn.getReaction("reaction"));
        assertTrue(spn.getReaction("reaction").getProducts().containsKey("P"));
        assertTrue(spn.getReaction("reaction").getReactants().containsKey("R"));
    }
    
    @Test
    public void testNegFeed() throws Exception {
        SBMLParser parser = new SBMLParser();
        
        parser.readFile("test/test/parser/neg_feedback.xml");
        
        parser.parse();
    }
    
    @Test
    public void testNegFeedWORead() throws Exception {
        SBMLParser parser = new SBMLParser();
        
        parser.readFile("test/test/parser/neg_feedback_wo_read.xml");
        
        parser.parse();        
    }
    
    // http://sandbox.kidstrythisathome.com/erdos/
    @Test
    public void testBioModelRepressilator() throws Exception {
        SBMLParser parser = new SBMLParser();
        parser.readFile("test/test/parser/BIOMD0000000012.xml");
        StochasticPetriNet spn = parser.parse(); 
        System.out.println(spn.toGraphviz());

        /*File dot = File.createTempFile("graph", ".dot");
        FileOutputStream out = new FileOutputStream(dot);
        out.write(spn.toGraphviz().getBytes());
        out.close();
        
        Runtime rt = Runtime.getRuntime();
        String[] args = {"/usr/local/bin/neato", "-Tpdf", dot.getAbsolutePath(), "-o", "test/test/sbml/BIOMD0000000012.pdf"};
        Process p = rt.exec(args);
        p.waitFor();*/                
    }
    
    @Test
    public void testMalformedInput() throws FileNotFoundException, IOException {
        SBMLParser parser = new SBMLParser();
        parser.readFile("test/test/parser/malformed.xml");
        parser.parse(); 
    }

}
