package test.data;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import dk.dtu.sb.data.SimulationPoint;

public class DataPointTest {
    
    private SimulationPoint dummyPoint(double time) {
        HashMap<String, Integer> markings = new HashMap<String, Integer>();
        markings.put("S", 1);
        markings.put("T", 4);
        return new SimulationPoint(time, markings);
    }

    @Test
    public void testSimulationPointTime() {
        assertEquals(1, dummyPoint(1.0).getTime(), 0);
    }
    
    @Test
    public void testSimulationPointMarking() {
        assertEquals(4, dummyPoint(1.0).getMarking("T"), 0);
        assertEquals(1, dummyPoint(1.0).getMarking("S"), 0);
    }
    
    @Test
    public void testSimulationPointMarkings() {
        Map<String, Integer> m = dummyPoint(1.0).getMarkings();
        assertEquals(2, m.size());
    }
    
    @Test
    public void testSimulationPointsEquals() {
        SimulationPoint sp1 = dummyPoint(1.0);
        SimulationPoint sp2 = dummyPoint(1.2);
        SimulationPoint sp3 = dummyPoint(1.0);
        assertTrue(sp1.equals(sp3));
        assertFalse(sp1.equals(sp2));
        assertFalse(sp2.equals(sp3));
    }
    
    @Test
    public void testSimulationPointSpecies() {
        Set<String> species = dummyPoint(1.0).getSpecies();
        assertTrue(species.contains("T"));
        assertTrue(species.contains("S"));
        assertFalse(species.contains("P"));
    }
}
