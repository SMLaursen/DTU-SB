package dk.dtu.sb.GUI;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.FlowLayout;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;

public class Main {

    private JFrame frame;
    
    private static final int HEIGHT = 600;
    private static final int WIDTH = 1000;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Main() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        
        JPanel leftPanel = new LeftPanel();
        frame.getContentPane().add(leftPanel, BorderLayout.WEST);
        
        JPanel centerPanel = new CenterPanel();
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
        
        JPanel rightPanel = new RightPanel();
        frame.getContentPane().add(rightPanel, BorderLayout.EAST);
        
    }

}
