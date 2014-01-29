package test.aig;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.github.qtstc.Formula;

import dk.dtu.techmap.AIG;

public class testAIG {
	@Test
    public void testSimple() throws IOException {
        Formula f = Formula.read(new BufferedReader(new FileReader("test/test/aig/simple.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        
        AIG g1 = new AIG(f.toString()); 
        
        System.out.println(g1.treeToString());
        
        //Validate structure of AIG which can have the following 4 permutations : 
        assertTrue(g1.treeToString().equals("O = (Not(And(Not(And(B()A()))C())))") ||
        		   g1.treeToString().equals("O = (Not(And(Not(And(A()B()))C())))") ||
        		   g1.treeToString().equals("O = (Not(And(C()Not(And(B()A())))))") ||
        		   g1.treeToString().equals("O = (Not(And(C()Not(And(A()B())))))"));
    }
	
	@Test
    public void testDifficult() throws IOException {
        Formula f = Formula.read(new BufferedReader(new FileReader("test/test/aig/input.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        AIG g = new AIG(f.toString());   
        System.out.println(f);
       
    }
	
	
	
}
