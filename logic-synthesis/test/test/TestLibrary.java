package test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.models.SBGate;

public class TestLibrary {

    @Test
    public void testInsert() throws SQLException {
        assertNull(Library.getGatesWithOutput("GFP"));
        
        Library.insert(new SBGate(0, "", 0, new String[]{""}, new String[]{""}, "GFP", "", 0));
        
        assertNotNull(Library.getGatesWithOutput("GFP"));
        assertEquals(1, Library.getGatesWithOutput("GFP").size());
    }
    
    @Test
    public void testInsertSorting() throws SQLException {
        Library.clear();
        assertNull(Library.getGatesWithOutput("GFP"));
        
        Library.insert(new SBGate(0, "", 1, new String[]{""}, new String[]{""}, "GFP", "", 0));
        Library.insert(new SBGate(1, "", 0, new String[]{""}, new String[]{""}, "GFP", "", 0));
        
        assertNotNull(Library.getGatesWithOutput("GFP"));
        assertEquals(2, Library.getGatesWithOutput("GFP").size());
        assertEquals(1, Library.getGatesWithOutput("GFP").get(0).id);
        assertEquals(0, Library.getGatesWithOutput("GFP").get(1).id);
    }

}
