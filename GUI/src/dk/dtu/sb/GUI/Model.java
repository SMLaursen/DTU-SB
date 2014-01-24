package dk.dtu.sb.GUI;

import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.event.SwingPropertyChangeSupport;

import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.models.SBGate;
import dk.dtu.sb.Parameters;
import dk.dtu.sb.data.SimulationResult;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;
import dk.dtu.sb.spn.StochasticPetriNet;

public class Model {
    
    public static final String EVENT_SBML_FILE_LOADED = "sbml_loaded";
    public static final String EVENT_LOG = "log_msg";
    public static final String EVENT_START_SIMULATION = "start_simulation";
    public static final String EVENT_SIMULATION_DONE = "simulation_done";
    
    private SwingPropertyChangeSupport propChangeFirer;
    
    public Parameters parameters = new Parameters();
    private StochasticPetriNet spn = new StochasticPetriNet();
    private SimulationResult simData;
    public final List<SBGate> library = Library.getAllParts();
    
    public int simulationNo = 1;

    public Model() {
        propChangeFirer = new SwingPropertyChangeSupport(this);
        
    }

    public void addListener(PropertyChangeListener prop) {
        propChangeFirer.addPropertyChangeListener(prop);
    }
    
    public void setSBML(String fileName) {
        SBMLParser parser = new SBMLParser();
        try {
            parser.readFile(fileName);
            spn = parser.parse();
            propChangeFirer.firePropertyChange(EVENT_SBML_FILE_LOADED, "", "new");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }
    
    public void startSimulation() {
        propChangeFirer.firePropertyChange(EVENT_START_SIMULATION, "", "new");
        
        (new SwingWorker<Boolean, Boolean>() {

            @Override
            protected Boolean doInBackground() throws Exception {
                Simulator simulator = new Simulator(spn, parameters);
                simulator.simulate();
                simData = simulator.getOutput();
                return true;
            }
            
            @Override
            protected void done() {
                propChangeFirer.firePropertyChange(EVENT_SIMULATION_DONE, "", "new");
                simulationNo++;
            }
        }).execute();
    }
    
    public StochasticPetriNet getSPN() {
        return spn;
    }
    
    public SimulationResult getSimulationResult() {
        return simData;
    }
    
    public void log(String msg) {
        propChangeFirer.firePropertyChange(EVENT_LOG, "", msg);
    }
}
