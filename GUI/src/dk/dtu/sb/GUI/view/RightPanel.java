package dk.dtu.sb.GUI.view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class RightPanel extends JPanel {

    public ParametersPanel parametersPanel;
    public JButton saveButton;
    public JButton loadButton;
    
    /**
     * Create the panel.
     */
    public RightPanel() {
        setBorder(new EmptyBorder(5, 0, 0, 0));
        setLayout(new BorderLayout(0, 0));
        
        JPanel panel_1 = new JPanel();
        add(panel_1, BorderLayout.SOUTH);
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        saveButton = new JButton("Save");
        panel_1.add(saveButton);
        
        loadButton = new JButton("Load");
        panel_1.add(loadButton);
        
        JLabel lblSimulationParameters = new JLabel("Simulation Parameters");
        lblSimulationParameters.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        add(lblSimulationParameters, BorderLayout.NORTH);
        
        parametersPanel = new ParametersPanel();
        add(parametersPanel, BorderLayout.CENTER);

    }

}
