package test.main;
import static org.junit.Assert.*;

import org.junit.Test;

import ch.qos.logback.classic.Level;
import test.StdOutTester;
import dk.dtu.sb.Main;
import dk.dtu.sb.Util;


public class MainTest extends StdOutTester {

    @Test
    public void testHelpText() {
        String[] args = new String[]{};
        Main.main(args);
        assertTrue(!out.toString().isEmpty());
    }
    
    @Test
    public void testVerbosityLevel() {
        String[] args = new String[]{"-v=0"};
        Main.main(args);
    }

    @Test
    public void testLoggingDebug() {
        String[] args = new String[]{"-d"};
        Main.main(args);
        assertEquals(Level.DEBUG, Util.log.getLevel());
    }
    
    @Test
    public void testLoggingError() {
        String[] args = new String[]{"-v=5"};
        Main.main(args);
        assertEquals(Level.ERROR, Util.log.getLevel());
    }
    
    @Test
    public void testInvalidVerbosity() {
        String[] args = new String[]{"-v=f"};
        Main.main(args);
        assertTrue(err.toString().contains("Please enter an integer as verbosity level"));
    }
    
    @Test
    public void testNoInput() {
        String[] args = new String[]{"-d"};
        Main.main(args);
        assertTrue(out.toString().contains("You have to provide either a properties file as input or specify a file as input."));
    }
    
    @Test
    public void testSimpleWrongInput() {
        String[] args = new String[]{"-f=test/test/main/fail.xml"};
        Main.main(args);
        assertTrue(out.toString().contains("Input file: test/test/main/fail.xml was not found."));       
    }
    
    @Test
    public void testSimpleInput() {
        System.setOut(stdout);
        String[] args = new String[]{"-d","-f=test/test/main/simple.xml"};
        Main.main(args);     
    }
    
    @Test
    public void testSimpleProperties() {
        System.setOut(stdout);
        String[] args = new String[]{"-d", "-rprop=test/test/main/simple.properties"};
        Main.main(args);     
    }
    
    @Test
    public void testSimMainProperties() {
        System.setOut(stdout);
        String[] args = new String[]{"-d", "-rprop=test_sim_main.properties"};
        Main.main(args);     
    }
}
