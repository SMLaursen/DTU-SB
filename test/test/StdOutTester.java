package test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;

public class StdOutTester {
    protected final PrintStream stdout = System.out;
    protected final PrintStream stderr = System.err;
    protected final ByteArrayOutputStream out = new ByteArrayOutputStream();
    protected final ByteArrayOutputStream err = new ByteArrayOutputStream();
    
    @Before
    public void setUp() throws UnsupportedEncodingException {
        System.setOut(new PrintStream(out, true, "UTF-8"));
        System.setErr(new PrintStream(err, true, "UTF-8"));
    }
    
    @After
    public void resetStreams() {
        System.setOut(stdout);
        System.setErr(stderr);
    }
}
