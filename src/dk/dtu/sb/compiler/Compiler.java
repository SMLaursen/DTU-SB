package dk.dtu.sb.compiler;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.StochasticPetriNet;

/**
 * 
 */
public class Compiler {

    private StochasticPetriNet spn;
    private Parameters params;

    /**
     * 
     * @param spn
     */
    public Compiler(StochasticPetriNet spn) {
        this.spn = spn;
    }

    /**
     * 
     * @param spn
     * @param params
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
            if (params != null) {
                Class<?> compilerClass;
                AbstractCompiler compiler;
                for (String compilerName : params.getCompilers()) {
                    currentCompiler = compilerName;
                    compilerClass = Class.forName(compilerName);

                    compiler = (AbstractCompiler) compilerClass.newInstance();
                    spn = compiler.compile(spn);
                }
            }

            currentCompiler = getClass().getCanonicalName();
            verifySPN();

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

    private void verifySPN() throws CompilerException {

    }
}
