package test.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import test.StdOutTester;
import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.parser.SBMLParser;

public class SBMLParserTest extends StdOutTester {
    
    @Test
    public void testSimple() throws Exception {
        SBMLParser parser = new SBMLParser();
        
        parser.readFile("test/test/parser/simple.xml");
        //resetStreams();
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
        resetStreams();
        System.out.println(parser.parse());
    }
    
    // http://sandbox.kidstrythisathome.com/erdos/
    @Test
    public void testBioModelRepressilator() throws Exception {
        //resetStreams();
        
        SBMLParser parser = new SBMLParser();
        parser.readFile("test/test/parser/BIOMD0000000012.xml");
        StochasticPetriNet spn = parser.parse(); 
        System.out.println(spn.toGraphviz());

        saveDotAsPdf(spn.toGraphviz(), "BIOMD0000000012.pdf");             
    }
    
    @Test
    public void testMalformedInput() throws FileNotFoundException, IOException {
        SBMLParser parser = new SBMLParser();
        parser.readFile("test/test/parser/malformed.xml");
        parser.parse();
        
        assertTrue(err.toString().contains("An error occurred when parsing the SBML file:"));
    }
    
    @Test
    public void testBioModelRepressilator2() throws Exception {
        resetStreams();
        
        SBMLParser parser = new SBMLParser();
        parser.readFile("test/test/parser/BIOMD0000000412.xml");
        StochasticPetriNet spn = parser.parse(); 
        System.out.println(spn);

        saveDotAsPdf(spn.toGraphviz(), "BIOMD0000000412.pdf");         
    }
    
    @Test
    public void testBioModelNegFeedbackOscillator() throws Exception {
        resetStreams();
        
        SBMLParser parser = new SBMLParser();
        parser.readFile("test/test/parser/BIOMD0000000010.xml");
        StochasticPetriNet spn = parser.parse(); 
        System.out.println(spn);

        saveDotAsPdf(spn.toGraphviz(), "BIOMD0000000010.pdf");         
    }
    
    private void saveDotAsPdf(String dot, String filename) throws IOException, InterruptedException {
        if (System.getProperty("os.name").equals("Mac OS X")) {
            File dotFile = File.createTempFile("graph", ".dot");
            FileOutputStream out = new FileOutputStream(dotFile);
            out.write(dot.getBytes());
            out.close();
            
            Runtime rt = Runtime.getRuntime();
            String[] args = {"/usr/local/bin/dot", "-Tpdf", dotFile.getAbsolutePath(), "-o", "test/test/parser/"+filename};
            Process p = rt.exec(args);
            p.waitFor();
        } 
    }

}
