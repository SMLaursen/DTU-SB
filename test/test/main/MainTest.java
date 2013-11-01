package test.main;
import static org.junit.Assert.*;

import org.junit.Test;

import dk.dtu.sb.Main;


public class MainTest {

    @Test
    public void testHelpText() {
        String[] args = new String[]{};
        Main.main(args);
    }

}
