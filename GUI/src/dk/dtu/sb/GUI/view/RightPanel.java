package dk.dtu.sb.GUI.view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class RightPanel extends JPanel {

    public ParametersPanel parametersPanel;
    public JButton saveButton;
    public JButton loadButton;
    private JPanel proteinPanel;
    private JLabel lblProteinConcentrations;
    private JScrollPane scrollPane;
    
    /**
     * Create the panel.
     */
    public RightPanel() {
        setBorder(new EmptyBorder(5, 0, 0, 0));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{220, 0};
        gridBagLayout.rowHeights = new int[]{10, 10, 164, 20, 100};
        gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 4.9E-324};
        setLayout(gridBagLayout);
        
        JLabel lblSimulationParameters = new JLabel("Simulation Parameters");
        lblSimulationParameters.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSimulationParameters.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        GridBagConstraints gbc_lblSimulationParameters = new GridBagConstraints();
        gbc_lblSimulationParameters.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblSimulationParameters.anchor = GridBagConstraints.NORTH;
        gbc_lblSimulationParameters.insets = new Insets(0, 0, 5, 0);
        gbc_lblSimulationParameters.gridx = 0;
        gbc_lblSimulationParameters.gridy = 0;
        add(lblSimulationParameters, gbc_lblSimulationParameters);
        
        JPanel panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_1.anchor = GridBagConstraints.NORTH;
        gbc_panel_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 1;
        add(panel_1, gbc_panel_1);
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        saveButton = new JButton("Save");
        panel_1.add(saveButton);
        
        loadButton = new JButton("Load");
        panel_1.add(loadButton);
        
        parametersPanel = new ParametersPanel();
        parametersPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        GridBagConstraints gbc_parametersPanel = new GridBagConstraints();
        gbc_parametersPanel.fill = GridBagConstraints.BOTH;
        gbc_parametersPanel.insets = new Insets(0, 0, 5, 0);
        gbc_parametersPanel.gridx = 0;
        gbc_parametersPanel.gridy = 2;
        add(parametersPanel, gbc_parametersPanel);
        
        lblProteinConcentrations = new JLabel("Initial species concentrations");
        lblProteinConcentrations.setAlignmentY(Component.TOP_ALIGNMENT);
        lblProteinConcentrations.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        GridBagConstraints gbc_lbl = new GridBagConstraints();
        gbc_lbl.anchor = GridBagConstraints.NORTHWEST;
        gbc_lbl.gridx = 0;
        gbc_lbl.gridy = 3;
        add(lblProteinConcentrations, gbc_lbl);
        
        scrollPane = new JScrollPane();
        scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(null);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 4;
        add(scrollPane, gbc_scrollPane);
        
        proteinPanel = new JPanel();
        proteinPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        proteinPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        scrollPane.setViewportView(proteinPanel);
        proteinPanel.setLayout(new BoxLayout(proteinPanel, BoxLayout.Y_AXIS));
    }
    
    public void addProteinConcentrations(Map<String, Integer> concentrations) {
        proteinPanel.removeAll();
        for (Entry<String, Integer> entry : concentrations.entrySet()) {
            proteinPanel.add(new ProteinLevelPanel(entry.getKey(), entry.getValue()));
        }
        proteinPanel.invalidate();
        proteinPanel.validate();
        proteinPanel.repaint();
    }
    
    public Map<String, Integer> getInitialConcentrations() {
        HashMap<String, Integer> levels = new HashMap<String, Integer>();
        for (int i = 0; i < proteinPanel.getComponentCount(); i++) {
            ProteinLevelPanel panel = (ProteinLevelPanel)proteinPanel.getComponent(i);
            levels.put(panel.getProteinName(), panel.getLevel());
        }
        return levels;
    }

}
