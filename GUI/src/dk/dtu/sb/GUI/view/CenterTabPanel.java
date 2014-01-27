package dk.dtu.sb.GUI.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class CenterTabPanel extends JPanel {
    
    public JTextPane description;
    public SimulationPanel simulation;
    public ImagePanel graph;
    public JTabbedPane tabs;

    /**
     * Create the panel.
     */
    public CenterTabPanel() {
        setLayout(new BorderLayout(0, 0));
        
        tabs = new JTabbedPane(SwingConstants.TOP);
        add(tabs, BorderLayout.CENTER);
        
        JPanel desc = new JPanel();
        tabs.addTab("Description", null, desc, null);
        desc.setLayout(new GridLayout(1, 0, 0, 0));
        
        description = new JTextPane();
        description.setEditable(false);
        desc.add(description);
     
        graph = new ImagePanel();
        tabs.addTab("SPN Graph", null, graph, null);
        
        simulation = new SimulationPanel();
        tabs.addTab("Simulation", null, simulation, null);

    }

}
