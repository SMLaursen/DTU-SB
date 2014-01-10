package dk.dtu.sb.compiler;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.spn.StochasticPetriNet;

/**
 * This class compiles a {@link StochasticPetriNet} with the chain of compilers
 * defined in {@link Parameters#getCompilers()}, finally the
 * {@link VerifyCompiler} compiles the resulting {@link StochasticPetriNet}.
 */
public class Compiler {

    private StochasticPetriNet spn;
    private Parameters params = new Parameters();

    /**
     * If this constructor is used, only the {@link VerifyCompiler} will be
     * used, as no chain of compilers is specified.
     * 
     * @param spn
     *            Input {@link StochasticPetriNet}.
     */
    public Compiler(StochasticPetriNet spn) {
        this.spn = spn;
        this.params = new Parameters();
    }

    /**
     * This constructor should be used if a chain of compilers is specified in
     * {@link Parameters#getCompilers()}.
     * 
     * @param spn
     *            Input {@link StochasticPetriNet}.
     * @param params
     *            {@link Parameters} object with specified chain of compilers.
     */
    public Compiler(StochasticPetriNet spn, Parameters params) {
        this.spn = spn;
        this.params = params;
    }

    /**
     * Compiles the {@link StochasticPetriNet} with the compilers specified in
     * {@link Parameters#getCompilers()}.
     * 
     * @return The resulting {@link StochasticPetriNet} or <code>null</code> if
     *         an error occurred.
     */
    public StochasticPetriNet compile() {
        String currentCompiler = "";

        try {
            AbstractCompiler compiler;
            Class<?> compilerClass;
            for (String compilerName : params.getCompilers()) {
                currentCompiler = compilerName;
                compilerClass = Class.forName(compilerName);

                compiler = (AbstractCompiler) compilerClass.newInstance();
                compiler.setParams(params);
                spn = compiler.compile(spn);
            }
            
            // Use framework compiler after the compiler-chain is done
            compiler = new VerifyCompiler();
            currentCompiler = compiler.getClass().getCanonicalName();
            compiler.setParams(params);
            spn = compiler.compile(spn);

        } catch (CompilerException e) {
            Util.log.error("An error occurred while the compiler: "
                    + currentCompiler + " compiled the SPN: " + e.getMessage());
            spn = null;
        } catch (Exception e) {
            Util.log.error("The compiler class: " + currentCompiler
                    + " could not be found.");
            spn = null;
        }

        return spn;
    }
    
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
