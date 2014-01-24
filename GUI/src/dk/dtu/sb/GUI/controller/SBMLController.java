package dk.dtu.sb.GUI.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.GUI.model.Model;
import dk.dtu.sb.GUI.view.LoadSBMLPanel;

public class SBMLController implements PropertyChangeListener {
    
    private LoadSBMLPanel view;
    private Model model;
    
    private final JFileChooser fileChooser = new JFileChooser();

    public SBMLController(LoadSBMLPanel view, Model model) {
        this.model = model;
        this.model.addListener(this);
        
        this.view = view;
        
        setUpViewEvents();
    }
    
    private void setUpViewEvents() {
        view.btnLoadFromSbml.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int returnVal = fileChooser.showOpenDialog(view);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();                    
                    model.setSBML(file.getAbsolutePath());
                }
            }
        });
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        // TODO Auto-generated method stub
        
    }

}
