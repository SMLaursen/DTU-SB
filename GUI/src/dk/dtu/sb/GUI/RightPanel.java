package dk.dtu.sb.GUI;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class RightPanel extends JPanel {

    /**
     * Create the panel.
     */
    public RightPanel() {
        setBorder(new EmptyBorder(5, 0, 0, 0));
        setLayout(new BorderLayout(0, 0));
        
        JPanel panel_1 = new JPanel();
        add(panel_1, BorderLayout.SOUTH);
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        JButton btnSave = new JButton("Save");
        panel_1.add(btnSave);
        
        JButton btnLoad = new JButton("Load");
        panel_1.add(btnLoad);
        
        JLabel lblSimulationParameters = new JLabel("Simulation Parameters");
        add(lblSimulationParameters, BorderLayout.NORTH);
        
        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);

    }

}
