package dk.dtu.sb.GUI.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

import dk.dtu.sb.GraphVizAPI;
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
        //Object newVal = arg0.getNewValue();

        if (propName.equals(Model.EVENT_SBML_FILE_LOADED)) {
            view.description.setText(model.getSPN().toString());

            view.graph.removeAll();
            view.graph.add(new JLabel("Loading..."));
            view.graph.repaint();
            
            (new SwingWorker<Boolean, Boolean>() {
                String type = "png";
                protected Boolean doInBackground() throws Exception {
                    GraphVizAPI gv = new GraphVizAPI();
                    gv.addln(model.getSPN().toGraphviz());
                    File out = new File(GraphVizAPI.OUT_PATH + "out." + type);
                    gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
                    return true;
                }

                protected void done() {
                    view.graph.setImage(GraphVizAPI.OUT_PATH + "out." + type);
                }
            }).execute();            
        }

        if (propName.equals(Model.EVENT_START_SIMULATION)
                || propName.equals(Model.EVENT_SIMULATION_DONE)) {
            view.tabs.setSelectedIndex(view.tabs.indexOfTab("Simulation"));
        }
    }

}
