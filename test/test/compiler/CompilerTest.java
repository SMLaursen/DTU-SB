package test.compiler;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.compiler.Compiler;
import dk.dtu.sb.spn.RateFunction;
import dk.dtu.sb.spn.Reaction;
import dk.dtu.sb.spn.Species;
import dk.dtu.sb.spn.StochasticPetriNet;

public class CompilerTest {

    @Test
    public void testCompilerWOChain() {
        Compiler compiler = new Compiler(dummySPN());
        StochasticPetriNet spn = compiler.compile();
        assertNotNull(spn);
    }

    @Test
    public void testCompilerChain1() {
        Parameters params = new Parameters();
        params.setCompilers(new String[] { "stub.NewCompiler" });
        Compiler compiler = new Compiler(dummySPN(), params);
        StochasticPetriNet spn = compiler.compile();

        assertNotNull(spn);
    }
    
    @Test
    public void testCompilerChain2() {
        Parameters params = new Parameters();
        params.setCompilers(new String[] { "stub.NewCompiler", "stub.NewCompiler2" });
        
        StochasticPetriNet spn = dummySPN();
                
        assertNull(spn.getReaction("compiler_r"));
        
        Compiler compiler = new Compiler(dummySPN(), params);
        spn = compiler.compile();

        assertNotNull(spn);
        
        assertNotNull(spn.getReaction("compiler_r"));
    }

    @Test
    public void testCompilerWrongCompiler() {
        Parameters params = new Parameters();
        params.setCompilers(new String[] { "stub.Dummy" });
        Compiler compiler = new Compiler(dummySPN(), params);
        StochasticPetriNet spn = compiler.compile();

        assertNull(spn);
    }

    @Test
    public void testCompilerChainWithError() {
        Parameters params = new Parameters();
        params.setCompilers(new String[] { "stub.ErrorCompiler" });
        Compiler compiler = new Compiler(dummySPN(), params);
        StochasticPetriNet spn = compiler.compile();

        assertNull(spn);
    }

    private StochasticPetriNet dummySPN() {
        StochasticPetriNet spn = new StochasticPetriNet();

        // species
        spn.addSpecies(new Species("A"));
        spn.addSpecies(new Species("B"));
        spn.addSpecies(new Species("AB"));

        // Reaction 1
        Reaction r1 = new Reaction("Composition", new RateFunction(0.9));
        r1.addReactant("A", 2);
        r1.addReactant("B");
        r1.addProduct("AB");
        spn.addReaction(r1);

        // Reaction 2
        Reaction r2 = new Reaction("Decomposition", new RateFunction(0.6));
        r2.addReactant("AB");
        r2.addProduct("A");
        r2.addProduct("B");

        spn.addReaction(r2);

        // Set initial markings
        spn.setInitialMarking("A", 1);
        spn.setInitialMarking("B", 1);
        spn.setInitialMarking("AB", 0);
        return spn;
    }
}
