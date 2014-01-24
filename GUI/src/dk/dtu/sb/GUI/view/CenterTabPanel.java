package dk.dtu.sb.GUI.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class CenterTabPanel extends JPanel {
    
    public JTextPane description;
    public SimulationPanel simulation;

    /**
     * Create the panel.
     */
    public CenterTabPanel() {
        setLayout(new BorderLayout(0, 0));
        
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        add(tabbedPane, BorderLayout.CENTER);
        
        JPanel desc = new JPanel();
        tabbedPane.addTab("Description", null, desc, null);
        desc.setLayout(new GridLayout(1, 0, 0, 0));
        
        description = new JTextPane();
        description.setEditable(false);
        desc.add(description);
        
        JPanel graph = new JPanel();
        tabbedPane.addTab("SPN Graph", null, graph, null);
        
        simulation = new SimulationPanel();
        tabbedPane.addTab("Simulation", null, simulation, null);

    }

}
