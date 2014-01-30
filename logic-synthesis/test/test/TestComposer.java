package test;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.qos.logback.classic.Level;
import dk.dtu.ls.library.ConcreteParts;
import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.SBGate;
import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.outputformatter.GraphGUI;
import dk.dtu.sb.simulator.Simulator;

public class TestComposer {

    @Test
    public void testCompose() {
        ConcreteParts.insertParts();
        SBGate or = Library.getById(1);
        assertNotNull(or.getSPN());
        assertEquals(5, or.getSPN().getSpeciess().size());
        
        SBGate and = Library.getById(3);        
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
        
        Parameters p = new Parameters();

        p.setSimIterations(2);
        p.setSimNoOfThreads(2);
        p.setSimStoptime(3000);
        p.setOutputStepCount(300);
        p.setSimThreshold(0.1);
        p.setSimMaxIterTime(120);
        p.setSimRateMode(Parameters.PARAM_SIM_RATE_MODE_CUSTOM);
        
        Util.log.setLevel(Level.DEBUG);
        Simulator simulator = new Simulator(result.getSPN(), p);
        simulator.simulate();

        GraphGUI graph = new GraphGUI();
        GraphGUI.outputProtein = result.outputProtein;
        graph.process(simulator.getOutput(), p);
    }

}
