package dk.dtu.sb.GUI.controller;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dk.dtu.sb.GUI.model.Model;
import dk.dtu.sb.GUI.view.LoadSBMLPanel;
import dk.dtu.sb.GUI.view.SimulationLoadingPanel;
import dk.dtu.sb.GUI.view.SimulationPanel;
import dk.dtu.sb.outputformatter.GraphPanel;

public class SimulationController implements PropertyChangeListener {

    private int no = 1;

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
            view.tabs.addTab("Simulation " + model.simulationNo,
                    new SimulationLoadingPanel());
        }

        if (propName.equals(Model.EVENT_SIMULATION_DONE)) {
            String title = "Simulation " + model.simulationNo;
            int index = view.tabs.indexOfTab(title);

            JPanel pnlTab = new JPanel(new GridBagLayout());
            pnlTab.setOpaque(false);
            JLabel lblTitle = new JLabel(title);
            JButton btnClose = new JButton("x");

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;

            pnlTab.add(lblTitle, gbc);

            gbc.gridx++;
            gbc.weightx = 0;
            pnlTab.add(btnClose, gbc);

            view.tabs.setTabComponentAt(index, pnlTab);
            view.tabs.setComponentAt(index, new GraphPanel(model.getSimulationResult(), ""));

            btnClose.addActionListener(new MyCloseActionHandler(title));
        }
    }

    private class MyCloseActionHandler implements ActionListener {

        private String tabName;

        public MyCloseActionHandler(String tabName) {
            this.tabName = tabName;
        }

        public String getTabName() {
            return tabName;
        }

        public void actionPerformed(ActionEvent evt) {

            int index = view.tabs.indexOfTab(getTabName());
            if (index >= 0) {

                view.tabs.removeTabAt(index);
            }

        }

    }

}
