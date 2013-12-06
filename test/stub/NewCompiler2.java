package stub;

import dk.dtu.sb.compiler.AbstractCompiler;
import dk.dtu.sb.compiler.CompilerException;
import dk.dtu.sb.data.RateFunction;
import dk.dtu.sb.data.Reaction;
import dk.dtu.sb.data.StochasticPetriNet;

public class NewCompiler2 extends AbstractCompiler {

    @Override
    public StochasticPetriNet compile(StochasticPetriNet spn)
            throws CompilerException {
        spn.addReaction(new Reaction("compiler_r", new RateFunction(1.0)));
        return spn;
    }

}
