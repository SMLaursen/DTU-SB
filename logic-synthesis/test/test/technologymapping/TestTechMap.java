package test.technologymapping;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.github.qtstc.Formula;

import dk.dtu.AIG.LogicGate;
import dk.dtu.AIG.TechnologyMapper;

public class TestTechMap {

	@Test
    public void test1() throws IOException {
        Formula f = Formula.read(new BufferedReader(new FileReader("test/test/aig/simple.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        
        TechnologyMapper t1 = new TechnologyMapper(f); // O = (C') + (A B)
        TechnologyMapper t2 = new TechnologyMapper("O =(I) + (C')");
        TechnologyMapper t3 = new TechnologyMapper("I = (B A)");
     
        //Ensure that t2 matches t1's pattern
        assertTrue(t1.isMatching(t2.getOutputGate(), t1.getOutputGate()));

        //Ensure there is only one unfinished mapping for I
        assertTrue(t1.getProgress().size()==1 && t1.getProgress().containsKey("I"));
       
        //Ensure that t3 fits the unfinished mapping and completes the technology mapping 
        // (complete when getProgress.isEmpty)
        assertTrue(t1.isMatching(t3.getOutputGate(), t1.getProgress().get("I")) && t1.getProgress().isEmpty());
	}
	
	@Test
    public void test2() throws IOException {
		Formula f = Formula.read(new BufferedReader(new FileReader("test/test/aig/simple.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        
        TechnologyMapper t1 = new TechnologyMapper(f); // O = (C') + (A B)
        TechnologyMapper t2 = new TechnologyMapper("O =(I) + (C')");
        TechnologyMapper t3 = new TechnologyMapper("I = (B C) + (C')");
        TechnologyMapper t4 = new TechnologyMapper("I = (C B)");
     
        //Ensure that t2 matches t1's pattern
        assertTrue(t1.isMatching(t2.getOutputGate(), t1.getOutputGate()));

        //Ensure there is only one unfinished mapping for I
        assertTrue(t1.getProgress().size()==1 && t1.getProgress().containsKey("I"));
        
        LogicGate temp = t1.getProgress().get("I");
        
        //Ensure that t3 dont fit the unfinished mapping
        assertFalse(t1.isMatching(t3.getOutputGate(), temp));
        
        //Ensure that t4 dont fit the unfinished mapping
        assertFalse(t1.isMatching(t4.getOutputGate(), temp));
    
	}
}
