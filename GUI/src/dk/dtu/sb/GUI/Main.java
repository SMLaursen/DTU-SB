package dk.dtu.sb.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

import dk.dtu.sb.GUI.controller.CenterTabsController;
import dk.dtu.sb.GUI.controller.ConsoleController;
import dk.dtu.sb.GUI.controller.ParametersController;
import dk.dtu.sb.GUI.controller.SBMLController;
import dk.dtu.sb.GUI.controller.SimulationController;
import dk.dtu.sb.GUI.view.CenterPanel;
import dk.dtu.sb.GUI.view.LeftPanel;
import dk.dtu.sb.GUI.view.RightPanel;

public class Main {

    private JFrame frame;
    
    private static final int HEIGHT = 600;
    private static final int WIDTH = 1000;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                try {
                	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        
        LeftPanel leftPanel = new LeftPanel();
        frame.getContentPane().add(leftPanel, BorderLayout.WEST);
        
        CenterPanel centerPanel = new CenterPanel();
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
        
        RightPanel rightPanel = new RightPanel();
        frame.getContentPane().add(rightPanel, BorderLayout.EAST);
        
        Model model = new Model();
                
        new ParametersController(rightPanel, model);
        new SBMLController(leftPanel.sbmlPanel, model);
        new ConsoleController(centerPanel, model);
        new CenterTabsController(centerPanel.topPanel, model);
        new SimulationController(centerPanel.topPanel.simulation, model);
    }

}
