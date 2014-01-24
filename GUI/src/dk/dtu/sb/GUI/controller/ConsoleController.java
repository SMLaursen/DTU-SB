package dk.dtu.sb.GUI.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import dk.dtu.sb.GUI.Console;
import dk.dtu.sb.GUI.Model;
import dk.dtu.sb.GUI.view.CenterPanel;

public class ConsoleController implements PropertyChangeListener {
    
    private CenterPanel view;
    private Model model;

    public ConsoleController(CenterPanel view, Model model) {
        this.model = model;
        this.model.addListener(this);
        
        this.view = view;
        
        Console.redirectOutput(view.consoleText);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        String propName = arg0.getPropertyName();
        Object newVal = arg0.getNewValue();

        if(propName.equals(Model.EVENT_LOG)){
            view.consoleText.setText((String)newVal + "\n" + view.consoleText.getText());
        }        
    }

}
