package dk.dtu.sb.compiler;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.spn.StochasticPetriNet;

/**
 * An abstract class defining what compilers specified in
 * {@link Parameters#getCompilers()} should implement.
 */
public abstract class AbstractCompiler {

    protected Parameters params = new Parameters();
    
    /**
     * This method compiles the {@link StochasticPetriNet} given.
     * 
     * @param spn
     *            The {@link StochasticPetriNet} to make transformations on.
     * @return The transformed {@link StochasticPetriNet}.
     * @throws CompilerException
     *             If an error occurs, a {@link CompilerException} is thrown.
     */
    public abstract StochasticPetriNet compile(StochasticPetriNet spn)
            throws CompilerException;
    
    /**
     * Used to set the {@link Parameters} object after instantiation.
     * 
     * @param params
     *            The {@link Parameters} with additional simulator parameters
     *            specified.
     */
    public void setParams(Parameters params) {
        this.params = params;
    }
}