package dk.dtu.sb.GUI.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class CenterPanel extends JPanel {

    public JTextArea consoleText;
    public CenterTabPanel topPanel;
    public JRadioButton rdbtnOn;
    public JRadioButton rdbtnOff;
    private JButton btnClearLog;

    /**
     * Create the panel.
     */
    public CenterPanel() {
        setBorder(new EmptyBorder(0, 0, 10, 0));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 50, 30, 450, 100, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 150, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0,
                Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0,
                Double.MIN_VALUE };
        setLayout(gridBagLayout);

        topPanel = new CenterTabPanel();

        GridBagConstraints gbc_top = new GridBagConstraints();
        gbc_top.fill = GridBagConstraints.BOTH;
        gbc_top.gridwidth = 4;
        gbc_top.gridx = 0;
        gbc_top.gridy = 0;
        add(topPanel, gbc_top);

        consoleText = new JTextArea();
        consoleText.setFont(new Font("Lucida Console", Font.PLAIN, 12));
        consoleText.setRows(6);
        DefaultCaret caret = (DefaultCaret) consoleText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JLabel lblDebugInfo = new JLabel("Debug info:");
        GridBagConstraints gbc_lblDebugInfo = new GridBagConstraints();
        gbc_lblDebugInfo.anchor = GridBagConstraints.WEST;
        gbc_lblDebugInfo.insets = new Insets(0, 0, 5, 5);
        gbc_lblDebugInfo.gridx = 0;
        gbc_lblDebugInfo.gridy = 1;
        add(lblDebugInfo, gbc_lblDebugInfo);

        rdbtnOn = new JRadioButton("On");
        rdbtnOn.setSelected(true);
        GridBagConstraints gbc_rdbtnOn = new GridBagConstraints();
        gbc_rdbtnOn.anchor = GridBagConstraints.WEST;
        gbc_rdbtnOn.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnOn.gridx = 1;
        gbc_rdbtnOn.gridy = 1;
        add(rdbtnOn, gbc_rdbtnOn);

        rdbtnOff = new JRadioButton("Off");
        GridBagConstraints gbc_rdbtnOff = new GridBagConstraints();
        gbc_rdbtnOff.anchor = GridBagConstraints.WEST;
        gbc_rdbtnOff.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnOff.gridx = 2;
        gbc_rdbtnOff.gridy = 1;
        add(rdbtnOff, gbc_rdbtnOff);

        btnClearLog = new JButton("Clear log");
        btnClearLog.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        GridBagConstraints gbc_btnClearLog = new GridBagConstraints();
        gbc_btnClearLog.anchor = GridBagConstraints.EAST;
        gbc_btnClearLog.insets = new Insets(0, 0, 5, 0);
        gbc_btnClearLog.gridx = 3;
        gbc_btnClearLog.gridy = 1;
        add(btnClearLog, gbc_btnClearLog);

        JScrollPane scrollPane = new JScrollPane(consoleText);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 4;
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 2;
        add(scrollPane, gbc_scrollPane);

        setupViewEvents();
    }

    private void setupViewEvents() {
        rdbtnOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                rdbtnOn.setSelected(false);
            }
        });

        rdbtnOn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                rdbtnOff.setSelected(false);
            }
        });

        btnClearLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                consoleText.setText("");
            }
        });
    }

}
