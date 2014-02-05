package test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.SBGate;

public class TestSBGate {

    @Test
    public void testAIG() {
        Library.loadLibrary();
        SBGate part = Library.getById(3);
        assertNotNull(part.getSPN());
        assertNotNull(part.getAIG());
    }

}
