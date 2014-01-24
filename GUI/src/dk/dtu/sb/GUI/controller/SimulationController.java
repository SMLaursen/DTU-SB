package dk.dtu.sb.GUI.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import dk.dtu.sb.GUI.Model;
import dk.dtu.sb.GUI.view.SimulationLoadingPanel;
import dk.dtu.sb.GUI.view.SimulationPanel;

public class SimulationController implements PropertyChangeListener {

    private SimulationPanel view;
    private Model model;

    public SimulationController(SimulationPanel view, Model model) {
        this.model = model;
        this.model.addListener(this);

        this.view = view;

        setUpViewEvents();
    }

    private void setUpViewEvents() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        String propName = arg0.getPropertyName();
        Object newVal = arg0.getNewValue();

        if (propName.equals(Model.EVENT_START_SIMULATION)) {
            view.addTab("Simulation " + model.simulationNo,
                    new SimulationLoadingPanel());

            view.selectTab("Simulation " + model.simulationNo);
        }

        if (propName.equals(Model.EVENT_SIMULATION_DONE)) {
            view.updateTab("Simulation " + model.simulationNo, model.getSimulationResult());
        }
    }
}
