package dk.dtu.sb.GUI;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class LeftPanel extends JPanel {

    /**
     * Create the panel.
     */
    public LeftPanel() {
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(280, 600));
        
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        add(tabbedPane, BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        tabbedPane.addTab("SBML", null, panel, null);
        
        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Truth Table", null, panel_1, null);

    }

}
