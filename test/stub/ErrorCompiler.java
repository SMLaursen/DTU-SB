package stub;

import dk.dtu.sb.compiler.CompilerException;
import dk.dtu.sb.compiler.CompilerInterface;
import dk.dtu.sb.spn.StochasticPetriNet;

public class ErrorCompiler implements CompilerInterface {

    @Override
    public StochasticPetriNet compile(StochasticPetriNet spn)
            throws CompilerException {
        throw new CompilerException("Dummy error");
    }

}
