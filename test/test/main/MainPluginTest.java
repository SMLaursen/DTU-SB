package test.main;
import org.junit.Test;

import dk.dtu.sb.Main;


public class MainPluginTest extends StdOutTester {
    
    @Test
    public void testSimpleParserProperties() {
        System.setErr(stderr);
        String[] args = new String[]{"-d", "-rprop=test/test/main/simple_parser.properties"};
        Main.main(args);
    }
    @Test
    public void testSimpleAlgorithmProperties() {
        System.setErr(stderr);
        String[] args = new String[]{"-d", "-rprop=test/test/main/simple_algo.properties"};
        Main.main(args);
    }
}
