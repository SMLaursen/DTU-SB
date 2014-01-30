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

@SuppressWarnings("serial")
public class TruthTablePanel extends JPanel {

    public JButton btnAddTruthTable;
    public JTextArea truthTableRaw;
    public JButton btnMinimise;
    public JTextArea textAreaMinimised;
    public JButton btnLoadSelectedDesign;
    public JList resultsList;
    private JLabel lblTheLastColumn;
    private JLabel lblRowsWithFalse;
    public JButton btnFindDesigns;

    /**
     * Create the panel.
     */
    public TruthTablePanel() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 324, 0 };
        gridBagLayout.rowHeights = new int[] { 29, 0, 0, 144, 29, 58, 27, 77, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                4.9E-324 };
        setLayout(gridBagLayout);

        btnAddTruthTable = new JButton("New truth table");
        GridBagConstraints gbc_btnAddTruthTable = new GridBagConstraints();
        gbc_btnAddTruthTable.insets = new Insets(0, 0, 5, 0);
        gbc_btnAddTruthTable.gridx = 0;
        gbc_btnAddTruthTable.gridy = 0;
        add(btnAddTruthTable, gbc_btnAddTruthTable);
        
        lblTheLastColumn = new JLabel("The last column is the output. ");
        lblTheLastColumn.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
        GridBagConstraints gbc_lblTheLastColumn = new GridBagConstraints();
        gbc_lblTheLastColumn.anchor = GridBagConstraints.WEST;
        gbc_lblTheLastColumn.insets = new Insets(0, 0, 5, 0);
        gbc_lblTheLastColumn.gridx = 0;
        gbc_lblTheLastColumn.gridy = 1;
        add(lblTheLastColumn, gbc_lblTheLastColumn);
        
        lblRowsWithFalse = new JLabel("Rows with false output can be omitted.");
        lblRowsWithFalse.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
        GridBagConstraints gbc_lblRowsWithFalse = new GridBagConstraints();
        gbc_lblRowsWithFalse.anchor = GridBagConstraints.WEST;
        gbc_lblRowsWithFalse.insets = new Insets(0, 0, 5, 0);
        gbc_lblRowsWithFalse.gridx = 0;
        gbc_lblRowsWithFalse.gridy = 2;
        add(lblRowsWithFalse, gbc_lblRowsWithFalse);

        truthTableRaw = new JTextArea();
        truthTableRaw.setFont(new Font("Lucida Console", Font.PLAIN, 13));
        truthTableRaw.setText("GFP IPTG lacI CI\n0001\n0011\n0101\n0111\n1000\n1010\n1100\n1111");
        GridBagConstraints gbc_truthTableRaw = new GridBagConstraints();
        gbc_truthTableRaw.fill = GridBagConstraints.BOTH;
        gbc_truthTableRaw.insets = new Insets(0, 0, 5, 0);
        gbc_truthTableRaw.gridx = 0;
        gbc_truthTableRaw.gridy = 3;
        add(truthTableRaw, gbc_truthTableRaw);
        
        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.anchor = GridBagConstraints.NORTH;
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 4;
        add(panel, gbc_panel);
        
        btnMinimise = new JButton("Minimise");
        panel.add(btnMinimise);
        
        btnFindDesigns = new JButton("Find designs");
        panel.add(btnFindDesigns);
        
        textAreaMinimised = new JTextArea();
        textAreaMinimised.setEditable(false);
        GridBagConstraints gbc_textArea = new GridBagConstraints();
        gbc_textArea.insets = new Insets(0, 0, 5, 0);
        gbc_textArea.fill = GridBagConstraints.BOTH;
        gbc_textArea.gridx = 0;
        gbc_textArea.gridy = 5;
        add(textAreaMinimised, gbc_textArea);

        JLabel lblNewLabel = new JLabel("Results");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 6;
        add(lblNewLabel, gbc_lblNewLabel);
        
        btnLoadSelectedDesign = new JButton("Load selected design");
        GridBagConstraints gbc_btnLoadSelectedDesign = new GridBagConstraints();
        gbc_btnLoadSelectedDesign.insets = new Insets(0, 0, 5, 0);
        gbc_btnLoadSelectedDesign.gridx = 0;
        gbc_btnLoadSelectedDesign.gridy = 8;
        add(btnLoadSelectedDesign, gbc_btnLoadSelectedDesign);

        JPanel resultDetailsPanel = new JPanel();
        GridBagConstraints gbc_resultDetailsPanel = new GridBagConstraints();
        gbc_resultDetailsPanel.fill = GridBagConstraints.BOTH;
        gbc_resultDetailsPanel.gridx = 0;
        gbc_resultDetailsPanel.gridy = 9;
        add(resultDetailsPanel, gbc_resultDetailsPanel);

        resultsList = new JList();
        GridBagConstraints gbc_resultsList = new GridBagConstraints();
        gbc_resultsList.insets = new Insets(0, 0, 5, 0);
        gbc_resultsList.fill = GridBagConstraints.BOTH;
        gbc_resultsList.gridx = 0;
        gbc_resultsList.gridy = 7;
        add(resultsList, gbc_resultsList);
    }

    

}
