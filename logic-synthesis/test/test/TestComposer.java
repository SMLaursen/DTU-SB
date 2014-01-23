package test;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.qos.logback.classic.Level;

import java.util.Map.*;

import dk.dtu.ls.library.ConcreteParts;
import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.models.SBGate;
import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.outputformatter.GraphGUI;
import dk.dtu.sb.simulator.Simulator;
import dk.dtu.sb.spn.Reaction;
import dk.dtu.sb.spn.Species;
import dk.dtu.sb.spn.StochasticPetriNet;

public class TestComposer {

    @Test
    public void testCompose() {
        SBGate or = new SBGate(
                1, 
                "1_or.xml", 
                ConcreteParts.cost(0, 2), 
                ConcreteParts.array("aTc", "Ara"),
                ConcreteParts.array(), 
                "CI", 
                "CI = (aTc' Ara ) + (aTc Ara' )", 
                4000
                );
        assertNotNull(or.getSPN());
        assertEquals(5, or.getSPN().getSpeciess().size());
        
        SBGate and = new SBGate(
                3, 
                "3_and.xml", 
                ConcreteParts.cost(0, 1), 
                ConcreteParts.array("IPTG", "lacI"),
                ConcreteParts.array(), 
                "Ara", 
                "Ara = (IPTG lacI )", 
                3000
                );        
        assertNotNull(and.getSPN());
        assertEquals(5, and.getSPN().getSpeciess().size());
        
        SBGate result = SBGate.compose(or, and);
        assertNotNull(result);
        assertEquals(3, result.inputProteins.size());
        assertEquals(1, result.intermediateProteins.size());
        assertEquals("Ara", result.intermediateProteins.get(0));
        assertEquals(9, result.getSPN().getSpeciess().size());
        
        result.getSPN().setInitialMarking("Ara", SBGate.LOW);
        result.getSPN().setInitialMarking("aTc", SBGate.HIGH);
        result.getSPN().setInitialMarking("IPTG", SBGate.HIGH);
        result.getSPN().setInitialMarking("lacI", SBGate.HIGH);
        
        /*System.out.println(result.toGraphviz());
        
        Parameters p = new Parameters();

        p.setSimIterations(2);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(10000);
        p.setOutputStepCount(100);
        p.setSimThreshold(0.1);
        p.setSimMaxIterTime(120);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);
        
        Util.log.setLevel(Level.DEBUG);
        Simulator simulator = new Simulator(result, p);
        simulator.simulate();

        GraphGUI graph = new GraphGUI();
        graph.process(simulator.getOutput(), p);  */
    }

}
