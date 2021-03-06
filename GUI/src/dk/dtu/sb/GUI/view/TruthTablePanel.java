package dk.dtu.sb.GUI.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import dk.dtu.ls.library.SBGate;

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
    public JButton btnFindFromTT;
    public SBGateDetailsPanel sbGateDetailsPanel;
    public JButton btnFindFromSop;
    private JComboBox comboBoxExamples;

    /**
     * Create the panel.
     */
    public TruthTablePanel() {
        createExamples();

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 250, 324, 0 };
        gridBagLayout.rowHeights = new int[] { 29, 0, 0, 124, 0, 30, 0, 0, 77, 0, 0};
        gridBagLayout.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, 4.9E-324 };
        setLayout(gridBagLayout);

        btnAddTruthTable = new JButton("New TT");
        btnAddTruthTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        GridBagConstraints gbc_btnAddTruthTable = new GridBagConstraints();
        gbc_btnAddTruthTable.gridwidth = 1;
        gbc_btnAddTruthTable.insets = new Insets(0, 0, 5, 5);
        gbc_btnAddTruthTable.gridx = 0;
        gbc_btnAddTruthTable.gridy = 0;
        add(btnAddTruthTable, gbc_btnAddTruthTable);

        ArrayList<String> titles = new ArrayList<String>();
        titles.addAll(examples.keySet());
        Collections.sort(titles);
        titles.add(0, "Examples...");
        comboBoxExamples = new JComboBox(titles.toArray());
        GridBagConstraints gbc_comboBoxExamples = new GridBagConstraints();
        gbc_comboBoxExamples.insets = new Insets(0, 0, 5, 0);
        gbc_comboBoxExamples.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxExamples.gridx = 1;
        gbc_comboBoxExamples.gridy = 0;
        add(comboBoxExamples, gbc_comboBoxExamples);

        lblTheLastColumn = new JLabel("The last column is the output. ");
        lblTheLastColumn.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
        GridBagConstraints gbc_lblTheLastColumn = new GridBagConstraints();
        gbc_lblTheLastColumn.gridwidth = 2;
        gbc_lblTheLastColumn.anchor = GridBagConstraints.WEST;
        gbc_lblTheLastColumn.insets = new Insets(0, 0, 5, 0);
        gbc_lblTheLastColumn.gridx = 0;
        gbc_lblTheLastColumn.gridy = 1;
        add(lblTheLastColumn, gbc_lblTheLastColumn);

        lblRowsWithFalse = new JLabel("Rows with false output can be omitted.");
        lblRowsWithFalse.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
        GridBagConstraints gbc_lblRowsWithFalse = new GridBagConstraints();
        gbc_lblRowsWithFalse.gridwidth = 2;
        gbc_lblRowsWithFalse.anchor = GridBagConstraints.WEST;
        gbc_lblRowsWithFalse.insets = new Insets(0, 0, 5, 0);
        gbc_lblRowsWithFalse.gridx = 0;
        gbc_lblRowsWithFalse.gridy = 2;
        add(lblRowsWithFalse, gbc_lblRowsWithFalse);

        truthTableRaw = new JTextArea();
        truthTableRaw.setFont(new Font("Lucida Console", Font.PLAIN, 13));
        truthTableRaw
                .setText("GFP IPTG lacI CI\n000 1\n001 1\n010 1\n011 1\n100 0\n101 0\n110 0\n111 1\n");
        GridBagConstraints gbc_truthTableRaw = new GridBagConstraints();
        gbc_truthTableRaw.gridwidth = 2;
        gbc_truthTableRaw.fill = GridBagConstraints.BOTH;
        gbc_truthTableRaw.insets = new Insets(0, 0, 5, 0);
        gbc_truthTableRaw.gridx = 0;
        gbc_truthTableRaw.gridy = 3;
        add(truthTableRaw, gbc_truthTableRaw);

        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.gridwidth = 2;
        gbc_panel.anchor = GridBagConstraints.NORTH;
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 4;
        add(panel, gbc_panel);

        btnMinimise = new JButton("To SoP");
        panel.add(btnMinimise);

        btnFindFromTT = new JButton("Find from TT");
        panel.add(btnFindFromTT);

        textAreaMinimised = new JTextArea();
        GridBagConstraints gbc_textArea = new GridBagConstraints();
        gbc_textArea.gridwidth = 2;
        gbc_textArea.insets = new Insets(0, 0, 5, 0);
        gbc_textArea.fill = GridBagConstraints.BOTH;
        gbc_textArea.gridx = 0;
        gbc_textArea.gridy = 5;
        add(textAreaMinimised, gbc_textArea);

        btnFindFromSop = new JButton("Find from SoP");
        GridBagConstraints gbc_btnFindFromSop = new GridBagConstraints();
        gbc_btnFindFromSop.gridwidth = 2;
        gbc_btnFindFromSop.insets = new Insets(0, 0, 5, 0);
        gbc_btnFindFromSop.gridx = 0;
        gbc_btnFindFromSop.gridy = 6;
        add(btnFindFromSop, gbc_btnFindFromSop);

        JLabel lblNewLabel = new JLabel("Results");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.gridwidth = 2;
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 7;
        add(lblNewLabel, gbc_lblNewLabel);

        btnLoadSelectedDesign = new JButton("Load selected design");
        GridBagConstraints gbc_btnLoadSelectedDesign = new GridBagConstraints();
        gbc_btnLoadSelectedDesign.gridwidth = 2;
        gbc_btnLoadSelectedDesign.insets = new Insets(0, 0, 5, 0);
        gbc_btnLoadSelectedDesign.gridx = 0;
        gbc_btnLoadSelectedDesign.gridy = 9;
        add(btnLoadSelectedDesign, gbc_btnLoadSelectedDesign);

        sbGateDetailsPanel = new SBGateDetailsPanel();
        GridBagConstraints gbc_resultDetailsPanel = new GridBagConstraints();
        gbc_resultDetailsPanel.gridwidth = 2;
        gbc_resultDetailsPanel.fill = GridBagConstraints.BOTH;
        gbc_resultDetailsPanel.gridx = 0;
        gbc_resultDetailsPanel.gridy = 10;
        add(sbGateDetailsPanel, gbc_resultDetailsPanel);

        resultsList = new JList();
        GridBagConstraints gbc_resultsList = new GridBagConstraints();
        gbc_resultsList.gridwidth = 2;
        gbc_resultsList.insets = new Insets(0, 0, 5, 0);
        gbc_resultsList.fill = GridBagConstraints.BOTH;
        gbc_resultsList.gridx = 0;
        gbc_resultsList.gridy = 8;
        add(resultsList, gbc_resultsList);

        setupComboViewEvents();
    }

    public void populateResultsList(List<SBGate> gates) {
        DefaultListModel model = new DefaultListModel();
        for (int i = 1; i <= gates.size(); i++) {
            model.addElement("Design " + i);
        }
        resultsList.setModel(model);
    }

    private void setupComboViewEvents() {
        comboBoxExamples.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String exampleTitle = (String)cb.getSelectedItem();
                
                if (examples.containsKey(exampleTitle)) {
                    populateResultsList(new ArrayList<SBGate>());
                    truthTableRaw.setText(examples.get(exampleTitle));
                    textAreaMinimised.setText("");
                }
            }
        });
    }

    HashMap<String, String> examples = new HashMap<String, String>();

    private void createExamples() {
        examples.put("Example 1, weak input", "GFP IPTG lacI CI\n000 1\n001 1\n010 1\n011 1\n100 0\n101 0\n110 0\n111 1\n");
        examples.put("Example 2, strong input", "GFP IPTG TetR CI\n000 1\n001 1\n010 1\n011 1\n100 0\n101 0\n110 0\n111 1\n");
        examples.put("CI = (GFP' Ara)", "GFP Ara CI\n00 1\n01 1\n10 0\n11 1");
        examples.put("NOR, YFP = (Ara' aTc')", "Ara aTc YFP\n00 1\n01 0\n10 0\n11 0");
    }

}
