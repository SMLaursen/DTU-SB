package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dk.dtu.sb.Parameters;

public class ParametersTest {

    @Test
    public void testCompilers() {
        Parameters params = new Parameters();
        params.setCompilers(new String[]{"stub.Compiler", "stub.Dummy"});
        assertTrue(params.getCompilers()[0].equals("stub.Compiler"));
        assertTrue(params.getCompilers()[1].equals("stub.Dummy"));
    }

}
