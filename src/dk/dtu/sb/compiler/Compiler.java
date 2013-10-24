package dk.dtu.sb.compiler;

import dk.dtu.sb.data.StochasticPetriNet;

public class Compiler {

    private StochasticPetriNet spn;
    
    public Compiler(StochasticPetriNet spn) {
        this.spn = spn;
    }
    
    public StochasticPetriNet compile() {
        return this.spn;
    }
}
