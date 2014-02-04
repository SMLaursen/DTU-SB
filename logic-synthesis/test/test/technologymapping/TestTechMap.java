package test.technologymapping;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import dk.dtu.ls.library.ConcreteParts;
import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.SBGate;
import dk.dtu.techmap.AIG;
import dk.dtu.techmap.TechnologyMapper;

public class TestTechMap {
	AIG primary = new AIG("O = (C') + (A B)");

    SBGate part1  = new SBGate(1, "O = (I) + (C')");
    SBGate part2  = new SBGate(2, "I = (B A)");
    SBGate part3  = new SBGate(3, "I = (Q)");
    SBGate part4  = new SBGate(4, "O = (B A) + (C')");
    SBGate part5  = new SBGate(5, "Q = (A B)");
    SBGate part6  = new SBGate(6, "I = (A B) + (C' D)");
    SBGate part7  = new SBGate(7, "I = (Q B)");
    SBGate part8  = new SBGate(8, "Q = A");
    SBGate part9  = new SBGate(9, "U = Q");
    SBGate part10 = new SBGate(10,"Q = U");

    // General purpose tests
    @Test
    public void test1() {
        Library.clear();
        
        // Test that p1 and p5 cannot be a solution
        Library.insert(part1);
        Library.insert(part5);
        TechnologyMapper techmap = new TechnologyMapper(primary);
        techmap.start();
        HashSet<SBGate> solution;
        assertTrue(techmap.getSolutions().isEmpty());
        

        // Test that p1 and p2 can be a solution
        Library.insert(part2);
        techmap.start();
        solution = techmap.getSolutions().get(0);
        System.out.println(solution);
        assertTrue(solution.contains(part1) && solution.contains(part2));
        
        
        // Test that p1,p3 and p5 can be a solution. (ADVANCED : p3 has to bee
        // mapped for "free")
        Library.insert(part3);
        Library.remove(part2);
        techmap.start();
        solution = techmap.getSolutions().get(0);
        System.out.println(solution);
        assertTrue(solution.contains(part1) 
        		&& solution.contains(part3)
        		&& solution.contains(part5));
        
        
        // Test that p4 (itself) can be a solution
        Library.insert(part4);
        Library.remove(part1);
        techmap.start();
        solution = techmap.getSolutions().get(0);
        System.out.println(solution);
        assertTrue(solution.contains(part4));
        
        
        // Test that the previous tries didn't cause any side-effects
        Library.remove(part3);
        Library.remove(part4);
        Library.insert(part6);
        techmap.start();
        System.out.println(solution);
        assertTrue(techmap.getSolutions().isEmpty());
        
        
        //test, that recursively "translates input nodes with one level of indirection
        Library.clear();
        Library.insert(part1);
        Library.insert(part7);
        Library.insert(part8);
        techmap.start();
        solution = techmap.getSolutions().get(0);
        System.out.println(solution);
        assertTrue(solution.contains(part1) 
        		&& solution.contains(part7)
        		&& solution.contains(part8));
        
    }
    
    // A bit more advanced.
    @Test
    public void test2(){
    	AIG secondary = new AIG("O = (A B) + (C D)");
        SBGate part11 = new SBGate(11, "O = (U) + (Q)");
        SBGate part12 = new SBGate(12, "Q = (B A)");
        SBGate part13 = new SBGate(13, "U = (C D)");
        SBGate part14 = new SBGate(14, "U = (C B)");
        SBGate part15 = new SBGate(15, "Z = (A B) + (U)");
        SBGate part16 = new SBGate(16, "O = Z");
        SBGate part17 = new SBGate(17, "Z = (Q) + (U)");
        
    	//Change to secondary
        TechnologyMapper techmap = new TechnologyMapper(secondary);
        Library.clear();
        Library.insert(part11);
        Library.insert(part12);
        Library.insert(part13);
        Library.insert(part14);
        techmap.start();
        HashSet<SBGate> solution = techmap.getSolutions().get(0);
        System.out.println(solution);
        assertTrue(solution.contains(part11) 
        		&& solution.contains(part12)
        		&& solution.contains(part13));
        
        
        //Check common error
        Library.clear();
        Library.insert(part15);
        Library.insert(part13);
        techmap.start();
        assertTrue(techmap.getSolutions().isEmpty());
        
        
        //Check output can be substitued as well
        Library.insert(part16);
        techmap.start();
        solution = techmap.getSolutions().get(0);
        System.out.println(solution);
        assertTrue(solution.contains(part13) &&
        		   solution.contains(part15) &&
        		   solution.contains(part16));
        
        //Check that many substitutions can occur
        Library.clear();
        Library.insert(part13);
        Library.insert(part17);
        Library.insert(part12);
        Library.insert(part16);
        techmap.start();
        solution = techmap.getSolutions().get(0);
        System.out.println(solution);
        assertTrue(solution.contains(part13) &&
     		   	   solution.contains(part17) &&
     		   	   solution.contains(part16) &&
     		       solution.contains(part12));        
    }
    
    
    //TODO : two levels of indirection at the input-gates.
    @Test
    public void test3(){
    	TechnologyMapper techmap = new TechnologyMapper(primary);
    	Library.clear();
        Library.insert(part7);
        Library.insert(part9);
        Library.insert(part10);
        techmap.start();
        //HashSet<SBGate> solution = techmap.getSolutions().get(0);
        //System.out.println(solution);
        //Currently failing
        assertFalse(techmap.getSolutions().isEmpty());
    }
    
    @Test
    public void testConcreteParts() {
        Library.clear();
        ConcreteParts.insertParts();
        AIG goal = new AIG("CI = (GFP) + (IPTG lacI)");
        System.out.println(goal.treeToString());
        TechnologyMapper techmap = new TechnologyMapper(goal);
        techmap.start();
        HashSet<SBGate> solution = techmap.getSolutions().get(0);
        System.out.println(solution);
        assertFalse(solution.isEmpty());
      
    }

}
