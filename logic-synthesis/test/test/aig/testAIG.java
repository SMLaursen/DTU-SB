package test.aig;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

import com.github.qtstc.Formula;

import dk.dtu.AIG.TechnologyMapper;

public class testAIG {
	@Test
    public void testSimple() throws IOException {
        Formula f = Formula.read(new BufferedReader(new FileReader("test/test/aig/simple.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        
        TechnologyMapper t1 = new TechnologyMapper(f); 
        
        System.out.println(t1.getOutputGate().subTreeToString());
        
        //Validate structure of AIG which can have the following 4 permutations : 
        assertTrue(t1.getOutputGate().subTreeToString().equals("O = (Not(And(Not(And(B()A()))C())))") ||
        		   t1.getOutputGate().subTreeToString().equals("O = (Not(And(Not(And(A()B()))C())))") ||
        		   t1.getOutputGate().subTreeToString().equals("O = (Not(And(C()Not(And(B()A())))))") ||
        		   t1.getOutputGate().subTreeToString().equals("O = (Not(And(C()Not(And(A()B())))))"));
    }
	
	@Test
    public void testDifficult() throws IOException {
        Formula f = Formula.read(new BufferedReader(new FileReader("test/test/aig/input.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        TechnologyMapper tech = new TechnologyMapper(f);   
        System.out.println(f);
        System.out.println(tech.isMatching(tech.getOutputGate(), tech.getOutputGate()));
    }
	
	
	
}
