package dk.dtu.sb.parser;

import dk.dtu.sb.data.StochasticPetriNet;

public abstract class Parser {

    protected StochasticPetriNet spn = new StochasticPetriNet();
    
    public abstract StochasticPetriNet parse();
    public abstract void readFile(String filename) throws Exception;
}
