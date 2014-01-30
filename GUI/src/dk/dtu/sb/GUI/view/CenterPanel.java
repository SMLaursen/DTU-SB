package dk.dtu.sb.GUI.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;

public class CenterPanel extends JPanel {

    public JTextArea consoleText;
    public CenterTabPanel topPanel;

    /**
     * Create the panel.
     */
    public CenterPanel() {
        setBorder(new EmptyBorder(0, 0, 10, 0));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 450, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 150, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        topPanel = new CenterTabPanel();
        
        GridBagConstraints gbc_top = new GridBagConstraints();
        gbc_top.fill = GridBagConstraints.BOTH;
        gbc_top.gridx = 0;
        gbc_top.gridy = 0;
        add(topPanel, gbc_top);

        consoleText = new JTextArea();
        consoleText.setFont(new Font("Lucida Console", Font.PLAIN, 12));
        consoleText.setRows(6);
        DefaultCaret caret = (DefaultCaret)consoleText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        JScrollPane scrollPane = new JScrollPane(consoleText);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 1;
        add(scrollPane, gbc_scrollPane);
    }

}
