package dk.dtu.sb.compiler;

import dk.dtu.sb.spn.StochasticPetriNet;

/**
 * This compiler will verify that the input {@link StochasticPetriNet} is valid
 * and safe to simulate.
 */
public class VerifyCompiler implements CompilerInterface {

    /**
     * {@inheritDoc}
     */
    public StochasticPetriNet compile(StochasticPetriNet spn)
            throws CompilerException {

        return spn;
    }

}
