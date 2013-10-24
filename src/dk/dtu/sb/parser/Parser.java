package dk.dtu.sb.parser;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import dk.dtu.sb.data.StochasticPetriNet;

public abstract class Parser {

    protected StochasticPetriNet spn = new StochasticPetriNet();
    
    public abstract StochasticPetriNet parse();
    public abstract void readFile(String filename) throws XMLStreamException, IOException;
}
