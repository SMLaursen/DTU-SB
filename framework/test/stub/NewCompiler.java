package stub;

import dk.dtu.sb.compiler.CompilerException;
import dk.dtu.sb.compiler.AbstractCompiler;
import dk.dtu.sb.spn.StochasticPetriNet;

public class NewCompiler extends AbstractCompiler {

    @Override
    public StochasticPetriNet compile(StochasticPetriNet spn)
            throws CompilerException {
        
        return spn;
    }

}
