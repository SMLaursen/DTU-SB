package dk.dtu.sb.GUI.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.models.SBGate;
import dk.dtu.sb.GUI.Model;
import dk.dtu.sb.GUI.view.LoadSBMLPanel;

public class SBMLController implements PropertyChangeListener {
    
    private LoadSBMLPanel view;
    private Model model;
    
    private final JFileChooser fileChooser = new JFileChooser();

    public SBMLController(LoadSBMLPanel view, Model model) {
        this.model = model;
        this.model.addListener(this);
        
        this.view = view;
        
        ArrayList<String> parts = new ArrayList<String>();
        
        for (SBGate gate : model.library) {
            parts.add(gate.SOP);
        }
        
        this.view.populateLibrary(parts);
        
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
        
        view.btnLoadLibrary.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                SBGate gate = model.library.get(view.list.getSelectedIndex());
                model.setSBML(gate.sbmlFile);
            }
        });
        
        view.list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = view.list.getSelectedIndex();
                if (index != -1) {
                    view.setDetails(model.library.get(index));
                }
            }
        });
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        // TODO Auto-generated method stub
        
    }

}
