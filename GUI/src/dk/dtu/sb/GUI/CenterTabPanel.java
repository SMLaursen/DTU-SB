package dk.dtu.sb.GUI;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;

public class CenterTabPanel extends JPanel {

    /**
     * Create the panel.
     */
    public CenterTabPanel() {
        setLayout(new BorderLayout(0, 0));
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        add(tabbedPane, BorderLayout.CENTER);
        
        JPanel desc = new JPanel();
        tabbedPane.addTab("Description", null, desc, null);
        
        JPanel graph = new JPanel();
        tabbedPane.addTab("Graph", null, graph, null);
        
        JPanel simulation = new JPanel();
        tabbedPane.addTab("Simulation", null, simulation, null);

    }

}
