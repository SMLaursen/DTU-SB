package test.parser;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import dk.dtu.sb.parser.Parser;
import dk.dtu.sb.spn.StochasticPetriNet;

public class ParserTest {

    @Test
    public void testParserSetInput() {
        StubParser parser = new StubParser();
        assertEquals("", parser.getInput());

        parser.setInput("input");
        assertEquals("input", parser.getInput());
    }

    @Test
    public void testParserReadFile() throws FileNotFoundException, IOException {
        StubParser parser = new StubParser();
        assertEquals("", parser.getInput());

        parser.readFile("test/test/parser/dummy_input.txt");
        assertEquals("dummy", parser.getInput());
    }

    @Test(expected = FileNotFoundException.class)
    public void testParserReadFileNoFile() throws FileNotFoundException,
            IOException {
        StubParser parser = new StubParser();
        assertEquals("", parser.getInput());

        parser.readFile("test/test/parser/fail.txt");
    }

}

class StubParser extends Parser {

    @Override
    public StochasticPetriNet parse() {
        return spn;
    }

}
