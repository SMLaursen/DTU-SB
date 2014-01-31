package dk.dtu.sb.GUI.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import ch.qos.logback.classic.Level;
import dk.dtu.sb.Util;
import dk.dtu.sb.GUI.Model;
import dk.dtu.sb.GUI.view.CenterPanel;

public class ConsoleController implements PropertyChangeListener {
    
    private CenterPanel view;
    private Model model;

    public ConsoleController(CenterPanel view, Model model) {
        this.model = model;
        this.model.addListener(this);
        
        this.view = view;
        
        setupViewEvents();
    }
    
    private void setupViewEvents() {
        view.rdbtnOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Util.log.setLevel(Level.INFO);
            }
        });
        view.rdbtnOn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Util.log.setLevel(Level.DEBUG);
            }
        });
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        String propName = arg0.getPropertyName();
        Object newVal = arg0.getNewValue();

        if(propName.equals(Model.EVENT_LOG)){
            view.consoleText.append((String)newVal);
        }        
    }

}
