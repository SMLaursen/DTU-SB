package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import ch.qos.logback.classic.Level;
import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.SBGate;
import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.outputformatter.CSV;
import dk.dtu.sb.outputformatter.GraphGUI;
import dk.dtu.sb.simulator.Simulator;

public class TestComposer {

    @Test
    public void testCompose() {
        Library.loadLibrary();
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
        assertEquals("Ara", result.intermediateProteins.iterator().next());
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
    
    @Test
    public void testComposeAndSimulate() {
        Library.loadLibrary();
        SBGate or = Library.getById(11);
        SBGate and = Library.getById(3);
        SBGate neg = Library.getById(4);
        SBGate neg2 = new SBGate(neg);
        neg2.id = 100;
        neg2.getSPN().removeReaction("id_100_decay_aTc");
        assertNotNull(or.getSPN());
        assertNotNull(and.getSPN());
        assertNotNull(neg.getSPN());
        
        SBGate result = SBGate.compose(or, and);
        assertNotNull(result);
        result = SBGate.compose(result, neg);
        result = SBGate.compose(result, neg2);
        
        result.getSPN().setInitialMarking("GFP", SBGate.LOW);
        result.getSPN().setInitialMarking("IPTG", SBGate.LOW);
        result.getSPN().setInitialMarking("lacI", SBGate.LOW);
        
        /*try {
            saveDotAsPdf(result.getSPN().toGraphviz(), "stronger.pdf");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        
        Parameters p = new Parameters();

        p.setSimIterations(10);
        p.setSimNoOfThreads(4);
        p.setSimStoptime(6000);
        
        Util.log.setLevel(Level.DEBUG);
        Simulator simulator = new Simulator(result.getSPN(), p);
        simulator.simulate();
        
        CSV csv = new CSV();
        p.setOutputFilename("part-11-3-2x4-stable.csv");
        csv.process(simulator.getOutput(), p);

//        GraphGUI graph = new GraphGUI();
//        GraphGUI.outputProtein = result.outputProtein;
//        graph.process(simulator.getOutput(), p);
    }
    
    public static void saveDotAsPdf(String dot, String filename) throws IOException, InterruptedException {
        if (System.getProperty("os.name").equals("Mac OS X")) {
            File dotFile = File.createTempFile("graph", ".dot");
            FileOutputStream out = new FileOutputStream(dotFile);
            out.write(dot.getBytes());
            out.close();
            
            Runtime rt = Runtime.getRuntime();
            String[] args = {"/usr/local/bin/dot", "-Tpdf", dotFile.getAbsolutePath(), "-o", filename};
            Process p = rt.exec(args);
            p.waitFor();
        } 
    }

}
