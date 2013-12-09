package dk.dtu.sb.compiler;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.data.StochasticPetriNet;

/**
 * An interface defining what compilers specified in
 * {@link Parameters#getCompilers()} should implement.
 */
public interface CompilerInterface {

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
}