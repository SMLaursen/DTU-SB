package dk.dtu.sb.GUI.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;

public class CenterPanel extends JPanel {

    public JTextPane consoleText;
    public CenterTabPanel topPanel;
    
    /**
     * Create the panel.
     */
    public CenterPanel() {
        setBorder(new EmptyBorder(0, 0, 10, 0));
        setLayout(new BorderLayout(0, 0));
        
        topPanel = new CenterTabPanel();
        add(topPanel);
        
        consoleText = new JTextPane();
        consoleText.setMargin(new Insets(5, 5, 5, 5));
        consoleText.setFont(new Font("Lucida Console", Font.PLAIN, 13));
        consoleText.setEditable(false);
        consoleText.setPreferredSize(new Dimension(100, 100));
        consoleText.setText("Text");
        topPanel.add(consoleText, BorderLayout.SOUTH);

    }

}
