package dk.dtu.sb.GUI.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import dk.dtu.sb.GUI.Model;
import dk.dtu.sb.GUI.view.CenterTabPanel;

public class CenterTabsController implements PropertyChangeListener {

    private CenterTabPanel view;
    private Model model;

    public CenterTabsController(CenterTabPanel view, Model model) {
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

        if (propName.equals(Model.EVENT_SBML_FILE_LOADED)) {
            view.description.setText(model.getSPN().toString());
        }
        
        if (propName.equals(Model.EVENT_START_SIMULATION) || propName.equals(Model.EVENT_SIMULATION_DONE)) {
            view.tabs.setSelectedIndex(view.tabs.indexOfTab("Simulation"));
        }
    }

}
