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
        HashSet<SBGate> solution = techmap.start();
        assertNull(solution);

        // Test that p1 and p2 can be a solution
        Library.insert(part2);
        solution = techmap.start();
        assertNotNull(solution);
       
        // Test that p1,p3 and p5 can be a solution. (ADVANCED : p3 has to bee
        // mapped for "free")
        Library.insert(part3);
        Library.remove(part2);
        solution = techmap.start();
        assertNotNull(solution);
        
        // Test that p4 (itself) can be a solution
        Library.insert(part4);
        Library.remove(part1);
        solution = techmap.start();
        assertNotNull(solution);

        // Test that the previous tries didn't cause any side-effects
        Library.remove(part3);
        Library.remove(part4);
        Library.insert(part6);
        solution = techmap.start();
        assertNull(solution);
        
    }

    // More advanced test, that recursively "translates input nodes
    // with one level of indirection
    @Test
    public void test2() {
        TechnologyMapper techmap = new TechnologyMapper(primary);

        Library.clear();
        Library.insert(part1);
        Library.insert(part7);
        Library.insert(part8);
        HashSet<SBGate> solution = techmap.start();
        assertNotNull(solution);
    }
    
    //TODO : two levels of indirection at the input-gates.
    @Test
    public void test3(){
    	TechnologyMapper techmap = new TechnologyMapper(primary);
    	Library.clear();
        Library.insert(part7);
        Library.insert(part9);
        Library.insert(part10);
        HashSet<SBGate> solution = techmap.start();
        //Currently failing
        assertNotNull(solution);
    }
    
    @Test
    public void testConcreteParts() {
        Library.clear();
        ConcreteParts.insertParts();
        AIG goal = new AIG("CI = (GFP) + (IPTG lacI)");
        TechnologyMapper techmap = new TechnologyMapper(goal);
        HashSet<SBGate> solution = techmap.start();
        assertNotNull(solution);
        System.out.println(solution);
    }

}
