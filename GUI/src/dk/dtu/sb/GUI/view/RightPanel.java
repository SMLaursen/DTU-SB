package dk.dtu.sb.GUI.view;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import java.awt.Font;

import javax.swing.BoxLayout;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import java.awt.Component;
import javax.swing.SpringLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;

public class RightPanel extends JPanel {

    public ParametersPanel parametersPanel;
    public JButton saveButton;
    public JButton loadButton;
    private JPanel proteinPanel;
    private JLabel lblProteinConcentrations;
    
    /**
     * Create the panel.
     */
    public RightPanel() {
        setBorder(new EmptyBorder(5, 0, 0, 0));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{220, 0};
        gridBagLayout.rowHeights = new int[]{10, 10, 164, 164, 0};
        gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        
        JLabel lblSimulationParameters = new JLabel("Simulation Parameters");
        lblSimulationParameters.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSimulationParameters.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        GridBagConstraints gbc_lblSimulationParameters = new GridBagConstraints();
        gbc_lblSimulationParameters.anchor = GridBagConstraints.NORTH;
        gbc_lblSimulationParameters.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblSimulationParameters.insets = new Insets(0, 0, 5, 0);
        gbc_lblSimulationParameters.gridx = 0;
        gbc_lblSimulationParameters.gridy = 0;
        add(lblSimulationParameters, gbc_lblSimulationParameters);
        
        JPanel panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.anchor = GridBagConstraints.NORTH;
        gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
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
        
        proteinPanel = new JPanel();
        GridBagConstraints gbc_proteinPanel = new GridBagConstraints();
        gbc_proteinPanel.anchor = GridBagConstraints.NORTHWEST;
        gbc_proteinPanel.gridx = 0;
        gbc_proteinPanel.gridy = 3;
        add(proteinPanel, gbc_proteinPanel);
        proteinPanel.setLayout(new BoxLayout(proteinPanel, BoxLayout.Y_AXIS));
        
        lblProteinConcentrations = new JLabel("Initial protein concentrations");
        lblProteinConcentrations.setHorizontalAlignment(SwingConstants.LEFT);
        lblProteinConcentrations.setAlignmentY(Component.TOP_ALIGNMENT);
        lblProteinConcentrations.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblProteinConcentrations.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        proteinPanel.add(lblProteinConcentrations);
    }
    
    public void addProteinConcentrations(Map<String, Integer> concentrations) {
        for (int i = 1; i < proteinPanel.getComponentCount(); i++) {
            proteinPanel.remove(i);
        }
        for (Entry<String, Integer> entry : concentrations.entrySet()) {
            proteinPanel.add(new ProteinLevelPanel(entry.getKey(), entry.getValue()));
        }
    }
    
    public Map<String, Integer> getInitialConcentrations() {
        HashMap<String, Integer> levels = new HashMap<String, Integer>();
        for (int i = 1; i < proteinPanel.getComponentCount(); i++) {
            ProteinLevelPanel panel = (ProteinLevelPanel)proteinPanel.getComponent(i);
            levels.put(panel.getProteinName(), panel.getLevel());
        }
        return levels;
    }

}
