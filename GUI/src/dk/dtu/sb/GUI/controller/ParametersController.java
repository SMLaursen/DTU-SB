package dk.dtu.sb.GUI.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.GUI.model.Model;
import dk.dtu.sb.GUI.view.ParametersPanel;
import dk.dtu.sb.GUI.view.RightPanel;

public class ParametersController implements PropertyChangeListener {

    private RightPanel rightPanel;
    private ParametersPanel parametersPanel;
    private Model model;
    
    private final JFileChooser fileChooser = new JFileChooser();
    
    public ParametersController(RightPanel rightPanel, Model model) {
        this.model = model;
        this.model.addListener(this);
        
        this.rightPanel = rightPanel;
        this.parametersPanel = rightPanel.parametersPanel;
        
        setupView();
        
        setUpViewEvents();
    }
    
    private void setupView() {
        parametersPanel.simulations.setValue(model.parameters.getSimIterations());
        parametersPanel.stoptime.setValue(model.parameters.getSimStoptime());
        parametersPanel.timeout.setValue(model.parameters.getSimMaxIterTime());
        parametersPanel.outSteps.setValue(model.parameters.getOutputStepCount());
        parametersPanel.threshold.setValue(model.parameters.getSimThreshold());
        parametersPanel.cores.setValue(model.parameters.getSimNoOfThreads());
    }
    
    private void setUpViewEvents() {
        parametersPanel.simulations.addChangeListener(new ChangeListener() {            
            @Override
            public void stateChanged(ChangeEvent arg0) {
                
            }
        });
        
        rightPanel.loadButton.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int returnVal = fileChooser.showOpenDialog(rightPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();                    
                    model.parameters = new Parameters(file.getAbsolutePath());
                    setupView();
                }
            }
        });
        
        rightPanel.saveButton.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int returnVal = fileChooser.showSaveDialog(rightPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();                    
                    model.parameters.saveAsFile(file.getAbsolutePath());
                }
            }
        });
        
        parametersPanel.simButton.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                model.startSimulation();
                parametersPanel.simButton.setEnabled(false);
            }
        });
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        String propName = arg0.getPropertyName();
        Object newVal = arg0.getNewValue();

        if (propName.equals(Model.EVENT_SBML_FILE_LOADED)) {
            parametersPanel.simButton.setEnabled(true);
        }
        if (propName.equals(Model.EVENT_START_SIMULATION)) {            
            parametersPanel.stopSimButton.setEnabled(true);
        }
        
        if (propName.equals(Model.EVENT_SIMULATION_DONE)) { 
            parametersPanel.simButton.setEnabled(true);
            parametersPanel.stopSimButton.setEnabled(false);
        }
    }

}
