package dk.dtu.sb.GUI.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import java.awt.Font;

@SuppressWarnings("serial")
public class CenterTabPanel extends JPanel {
    
    public JTextPane description;
    public SimulationPanel simulation;
    public ImagePanel graph;
    public JTabbedPane tabs;
    private JScrollPane scrollPane;
    private JScrollPane scrollPane_1;

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
        
        scrollPane_1 = new JScrollPane();
        desc.add(scrollPane_1);
        
        description = new JTextPane();
        description.setFont(new Font("Lucida Console", Font.PLAIN, 13));
        scrollPane_1.setViewportView(description);
        description.setEditable(false);
     
        graph = new ImagePanel();
        scrollPane = new JScrollPane(graph);
        tabs.addTab("SPN Graph", null, scrollPane, null);
        
        simulation = new SimulationPanel();
        tabs.addTab("Simulation", null, simulation, null);

    }

}
