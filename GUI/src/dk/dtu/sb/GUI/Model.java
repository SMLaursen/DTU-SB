package dk.dtu.sb.GUI;

import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.SwingWorker;
import javax.swing.event.SwingPropertyChangeSupport;

import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.SBGate;
import dk.dtu.sb.Parameters;
import dk.dtu.sb.data.SimulationResult;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;
import dk.dtu.sb.spn.StochasticPetriNet;

public class Model {

    public static final String EVENT_SBML_FILE_LOADED = "sbml_loaded";
    public static final String EVENT_LOG = "log_msg";
    public static final String EVENT_START_SIMULATION = "start_simulation";
    public static final String EVENT_STOP_SIMULATION = "stop_simulation";
    public static final String EVENT_SIMULATION_DONE = "simulation_done";

    public static final int CURRENT_MODEL_LIBRARY = 100;
    public static final int CURRENT_MODEL_FILE = 200;

    private SwingPropertyChangeSupport propChangeFirer;
    private String sbmlFilename;
    private SimulationWorker simWorker;

    // properties
    public Parameters parameters = new Parameters();
    public final List<SBGate> library = Library.getAllParts();
    private StochasticPetriNet spn = new StochasticPetriNet();
    private SimulationResult simData;
    public int simulationNo = 1;
    public int currentLoadedModel = 0;
    public String outputProtein;
    public ArrayList<String> inputProteins;

    public Model() {
        propChangeFirer = new SwingPropertyChangeSupport(this);
    }

    public void addListener(PropertyChangeListener prop) {
        propChangeFirer.addPropertyChangeListener(prop);
    }

    public void setSBML(String fileName, int currentModel) {
        this.currentLoadedModel = currentModel;
        this.sbmlFilename = fileName;
        (new SwingWorker<Boolean, Boolean>() {

            @Override
            protected Boolean doInBackground() throws Exception {
                SBMLParser parser = new SBMLParser();
                try {
                    parser.readFile(sbmlFilename);
                    spn = parser.parse();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void done() {
                propChangeFirer.firePropertyChange(EVENT_SBML_FILE_LOADED, "",
                        "new");
                sbmlFilename = null;
            }
        }).execute();
    }

    public void setInitialMarkings(Map<String, Integer> markings) {
        for (Entry<String, Integer> entry : markings.entrySet()) {
            spn.setInitialMarking(entry.getKey(), entry.getValue());
        }
    }

    public void startSimulation() {
        propChangeFirer.firePropertyChange(EVENT_START_SIMULATION, "", "new");
        simWorker = new SimulationWorker();
        simWorker.execute();
    }

    public void stopSimulation() {
        (new SwingWorker<Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                simWorker.stopSimulation();
                return true;
            }
        }).execute();;
        //simWorker.stopSimulation();
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

    /**
     * Background Worker threads
     */
    class SimulationWorker extends SwingWorker<Boolean, Boolean> {

        Simulator simulator;
        boolean interrupted = false;

        public void stopSimulation() {
            interrupted = true;
            propChangeFirer
                    .firePropertyChange(EVENT_STOP_SIMULATION, "", "new");
            simulator.stopSimulation();
            cancel(true);
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            simulator = new Simulator(spn, parameters);
            simulator.simulate();
            simData = simulator.getOutput();
            return true;
        }

        @Override
        protected void done() {
            if (!interrupted) {
                propChangeFirer.firePropertyChange(EVENT_SIMULATION_DONE, "",
                        "new");
                simulationNo++;
            }
        }
    }
}
