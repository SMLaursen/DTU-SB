package dk.dtu.sb.GUI.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TruthTablePanel extends JPanel {

    public JButton btnAddTruthTable;
    public JTextArea truthTableRaw;
    public JButton btnMinimise;
    public JTextArea textAreaMinimised;
    public JButton btnLoadSelectedDesign;
    public JList resultsList;

    /**
     * Create the panel.
     */
    public TruthTablePanel() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 324, 0 };
        gridBagLayout.rowHeights = new int[] { 29, 144, 29, 58, 27, 77, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                4.9E-324 };
        setLayout(gridBagLayout);

        btnAddTruthTable = new JButton("New truth table");
        GridBagConstraints gbc_btnAddTruthTable = new GridBagConstraints();
        gbc_btnAddTruthTable.insets = new Insets(0, 0, 5, 0);
        gbc_btnAddTruthTable.gridx = 0;
        gbc_btnAddTruthTable.gridy = 0;
        add(btnAddTruthTable, gbc_btnAddTruthTable);

        truthTableRaw = new JTextArea();
        truthTableRaw.setFont(new Font("Lucida Console", Font.PLAIN, 13));
        truthTableRaw.setText("aTc Ara CI\n000\n011\n101\n110");
        GridBagConstraints gbc_truthTableRaw = new GridBagConstraints();
        gbc_truthTableRaw.fill = GridBagConstraints.BOTH;
        gbc_truthTableRaw.insets = new Insets(0, 0, 5, 0);
        gbc_truthTableRaw.gridx = 0;
        gbc_truthTableRaw.gridy = 1;
        add(truthTableRaw, gbc_truthTableRaw);
        
        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.anchor = GridBagConstraints.NORTH;
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 2;
        add(panel, gbc_panel);
        
        btnMinimise = new JButton("Minimise");
        panel.add(btnMinimise);
        
        JButton btnFindDesigns = new JButton("Find designs");
        panel.add(btnFindDesigns);
        
        textAreaMinimised = new JTextArea();
        textAreaMinimised.setEditable(false);
        GridBagConstraints gbc_textArea = new GridBagConstraints();
        gbc_textArea.insets = new Insets(0, 0, 5, 0);
        gbc_textArea.fill = GridBagConstraints.BOTH;
        gbc_textArea.gridx = 0;
        gbc_textArea.gridy = 3;
        add(textAreaMinimised, gbc_textArea);

        JLabel lblNewLabel = new JLabel("Results");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 4;
        add(lblNewLabel, gbc_lblNewLabel);
        
        btnLoadSelectedDesign = new JButton("Load selected design");
        GridBagConstraints gbc_btnLoadSelectedDesign = new GridBagConstraints();
        gbc_btnLoadSelectedDesign.insets = new Insets(0, 0, 5, 0);
        gbc_btnLoadSelectedDesign.gridx = 0;
        gbc_btnLoadSelectedDesign.gridy = 6;
        add(btnLoadSelectedDesign, gbc_btnLoadSelectedDesign);

        JPanel resultDetailsPanel = new JPanel();
        GridBagConstraints gbc_resultDetailsPanel = new GridBagConstraints();
        gbc_resultDetailsPanel.fill = GridBagConstraints.BOTH;
        gbc_resultDetailsPanel.gridx = 0;
        gbc_resultDetailsPanel.gridy = 7;
        add(resultDetailsPanel, gbc_resultDetailsPanel);

        resultsList = new JList();
        GridBagConstraints gbc_resultsList = new GridBagConstraints();
        gbc_resultsList.insets = new Insets(0, 0, 5, 0);
        gbc_resultsList.fill = GridBagConstraints.BOTH;
        gbc_resultsList.gridx = 0;
        gbc_resultsList.gridy = 5;
        add(resultsList, gbc_resultsList);
    }

    

}
