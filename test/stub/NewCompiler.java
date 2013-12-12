package stub;

import dk.dtu.sb.compiler.CompilerException;
import dk.dtu.sb.compiler.CompilerInterface;
import dk.dtu.sb.data.StochasticPetriNet;

public class NewCompiler implements CompilerInterface {

    @Override
    public StochasticPetriNet compile(StochasticPetriNet spn)
            throws CompilerException {
        
        return spn;
    }

}
