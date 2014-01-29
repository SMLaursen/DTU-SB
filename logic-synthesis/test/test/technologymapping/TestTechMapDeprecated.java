//package test.technologymapping;
//
//import static org.junit.Assert.*;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//import org.junit.Test;
//
//import com.github.qtstc.Formula;
//
//import dk.dtu.techmap.AIG;
//import dk.dtu.techmap.LogicGate;
//import dk.dtu.techmap.TechnologyMapper;
//
//public class TestTechMapDeprecated {
//
//	@Test
//    public void test1() throws IOException {
//        Formula f = Formula.read(new BufferedReader(new FileReader("test/test/aig/simple.txt")));
//        f.reduceToPrimeImplicants();
//        f.reducePrimeImplicantsToSubset();
//        
//        AIG g1 = new AIG(f); // O = (C') + (A B)
//        AIG g2 = new AIG("O =(I) + (C')");
//        AIG g3 = new AIG("I = (B A)");
//     
//        TechnologyMapper t = new TechnologyMapper(g1);
//        
//        //Ensure that t2 matches t1's pattern
//        assertTrue(t.isMatching(g2.getOutputGate(), g1.getOutputGate())!= null);
//
//        //Ensure there is only one unfinished mapping for I
//        assertTrue(t.getNext().size()==1 && t.getNext().containsKey("I"));
//       
//        //Ensure that t3 fits the unfinished mapping and completes the technology mapping 
//        // (complete when getProgress.isEmpty)
//        assertTrue(t.isMatching(g3.getOutputGate(), t.getNext().get("I")) && t.getNext().isEmpty());
//	}
//	
//	@Test
//    public void test2() throws IOException {
//		Formula f = Formula.read(new BufferedReader(new FileReader("test/test/aig/simple.txt")));
//        f.reduceToPrimeImplicants();
//        f.reducePrimeImplicantsToSubset();
//        
//        AIG g1 = new AIG(f); // O = (C') + (A B)
//        AIG g2 = new AIG("O =(I) + (C')");
//        AIG g3 = new AIG("I = (B C) + (C')");
//        AIG g4 = new AIG("I = (C B)");
//     
//        TechnologyMapper t = new TechnologyMapper(g1);
//        
//        //Ensure that t2 matches t1's pattern
//        assertTrue(t.isMatching(g2.getOutputGate(), g1.getOutputGate()));
//
//        //Ensure there is only one unfinished mapping for I
//        assertTrue(t.getNext().size()==1 && t.getNext().contains("I"));
//        
//        LogicGate temp = t.getNext().get("I");
//        
//        //Ensure that t3 dont fit the unfinished mapping
//        assertFalse(t.isMatching(g3.getOutputGate(), temp));
//        
//        //Ensure that t4 dont fit the unfinished mapping
//        assertFalse(t.isMatching(g4.getOutputGate(), temp));
//    
//	}
//}
