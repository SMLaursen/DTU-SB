package dk.dtu.sb.GUI.view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class SimulationLoadingPanel extends JPanel {

    /**
     * Create the panel.
     */
    public SimulationLoadingPanel() {
        setLayout(new BorderLayout(0, 0));
        
        JLabel lblSimulationStillRunning = new JLabel("Simulation still running...");
        lblSimulationStillRunning.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblSimulationStillRunning, BorderLayout.CENTER);

    }

}
