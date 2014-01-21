package test.aig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.github.qtstc.Formula;

import dk.dtu.AIG.AIG;

public class testAIG {
	@Test
    public void testSimple() throws IOException {
        Formula f = Formula.read(new BufferedReader(new FileReader("test/test/aig/simple.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        AIG g = new AIG(f);   
        System.out.println(g);
    }
	
	@Test
    public void testDifficult() throws IOException {
        Formula f = Formula.read(new BufferedReader(new FileReader("test/test/aig/input.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        AIG g = new AIG(f);   
        System.out.println(g);
    }
}
