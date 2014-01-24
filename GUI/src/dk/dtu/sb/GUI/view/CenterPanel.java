package dk.dtu.sb.GUI.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;

public class CenterPanel extends JPanel {

    public JTextArea consoleText;
    public CenterTabPanel topPanel;

    /**
     * Create the panel.
     */
    public CenterPanel() {
        setBorder(new EmptyBorder(0, 0, 10, 0));
        setLayout(new BorderLayout(0, 0));

        topPanel = new CenterTabPanel();
        add(topPanel);

        consoleText = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(consoleText);
        topPanel.add(scrollPane, BorderLayout.SOUTH);
    }

}
