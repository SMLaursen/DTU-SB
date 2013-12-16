package test.data;

import org.junit.Assert;
import org.junit.Test;

import dk.dtu.sb.data.AlgorithmResult;
import dk.dtu.sb.data.Markings;

public class AlgorithmResultTest {

    @Test
    public void testAlgorithmResult() {
        AlgorithmResult result = new AlgorithmResult();
        
        Markings markings = new Markings();
        markings.put("S", 2);        
        result.add(1, markings);
        
        markings.put("S", 3);
        result.add(1.2, markings);
        
        Assert.assertTrue(result.getSpecies().contains("S"));
        Assert.assertEquals(1.0, result.getSimulationPoints().getFirst().getTime(), 0);
        Assert.assertEquals(1.2, result.getSimulationPoints().getLast().getTime(), 0);
    } 
}
