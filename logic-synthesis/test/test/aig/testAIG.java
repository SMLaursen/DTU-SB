package test.aig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import com.github.qtstc.Formula;

import dk.dtu.AIG.TechnologyMapper;

public class testAIG {
	@Test
    public void testSimple() throws IOException {
        Formula f = Formula.read(new BufferedReader(new FileReader("test/test/aig/simple.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        System.out.println(f);
        
        TechnologyMapper t1 = new TechnologyMapper("O =(A B) +  (C D)");
        TechnologyMapper t2 = new TechnologyMapper(f);

        System.out.println(t1);
        System.out.println(t2);
        
        System.out.println(t1.isMatching(t2.getOutputGate(), t1.getOutputGate()));
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
