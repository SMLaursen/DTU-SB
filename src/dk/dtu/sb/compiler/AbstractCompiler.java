package dk.dtu.sb.compiler;

import dk.dtu.sb.data.StochasticPetriNet;

/**
 * 
 */
public abstract class AbstractCompiler {

    /**
     * 
     * @param spn
     * @return
     * @throws CompilerException
     */
    public abstract StochasticPetriNet compile(StochasticPetriNet spn)
            throws CompilerException;
}