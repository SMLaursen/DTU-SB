package test;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.dtu.ls.library.ConcreteParts;
import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.SBGate;
import dk.dtu.sb.spn.StochasticPetriNet;

public class TestLibrary {

    @Test
    public void testInsert() {
        assertNull(Library.getGatesWithOutput("GFP"));
        
        Library.insert(new SBGate(0, "", 0, ConcreteParts.array(), ConcreteParts.array(), "GFP", "", 0));
        
        assertNotNull(Library.getGatesWithOutput("GFP"));
        assertEquals(1, Library.getGatesWithOutput("GFP").size());
    }
    
    @Test
    public void testInsertSorting() {
        Library.clear();
        assertNull(Library.getGatesWithOutput("GFP"));
        
        Library.insert(new SBGate(0, "", 1, ConcreteParts.array(), ConcreteParts.array(), "GFP", "", 0));
        Library.insert(new SBGate(1, "", 0, ConcreteParts.array(), ConcreteParts.array(), "GFP", "", 0));
        
        assertNotNull(Library.getGatesWithOutput("GFP"));
        assertEquals(2, Library.getGatesWithOutput("GFP").size());
        assertEquals(1, Library.getGatesWithOutput("GFP").get(0).id);
        assertEquals(0, Library.getGatesWithOutput("GFP").get(1).id);
    }
    
    @Test
    public void testConcreteParts() {
        assertNull(Library.getGatesWithOutput("CI"));
        
        ConcreteParts.insertParts();
        
        assertNotNull(Library.getGatesWithOutput("CI"));
        assertEquals(2, Library.getGatesWithOutput("CI").size());
        
        StochasticPetriNet spn = Library.getGatesWithOutput("CI").get(0).getSPN();
        assertNotNull(spn.getSpecies("aTc"));
    }
    
    @Test
    public void testConcretePartsContent() {
        assertNull(Library.getGatesWithOutput("CI"));
        
        ConcreteParts.insertParts();
        
        assertNotNull(Library.getGatesWithOutput("CI"));
        assertEquals(2, Library.getGatesWithOutput("CI").size());
        
        SBGate gate = Library.getGatesWithOutput("CI").get(0);
        
        assertEquals(2, gate.inputProteins.size());
        assertEquals(0, gate.intermediateProteins.size());
        assertEquals("CI", gate.outputProtein);
    }

}
