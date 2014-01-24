package dk.dtu.sb.GUI.view;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

public class SimulationPanel extends JPanel {

    public JTabbedPane tabs;
    
    /**
     * Create the panel.
     */
    public SimulationPanel() {
        setLayout(new BorderLayout(0, 0));
        
        tabs = new JTabbedPane(JTabbedPane.TOP);
        add(tabs);

    }

}
