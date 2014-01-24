package dk.dtu.sb.GUI.view;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class LeftPanel extends JPanel {

    public LoadSBMLPanel sbmlPanel;
    
    /**
     * Create the panel.
     */
    public LeftPanel() {
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(280, 600));
        
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        add(tabbedPane, BorderLayout.CENTER);
        
        sbmlPanel = new LoadSBMLPanel();
        tabbedPane.addTab("SBML", null, sbmlPanel, null);
        
        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Truth Table", null, panel_1, null);

    }

}
