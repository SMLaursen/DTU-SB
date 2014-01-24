package dk.dtu.sb.outputformatter;

import javax.swing.JFrame;

import dk.dtu.sb.data.SimulationResult;

/**
 * A concrete implementation of the {@link AbstractOutputFormatter}. Shows a
 * Java Swing GUI with a graph of the simulation results.
 */
public class GraphGUI extends AbstractOutputFormatter {
    
    public static String outputProtein = null;
    
    public void process(SimulationResult plotData) {
        JFrame frame = new JFrame();
        frame.add(new GraphPanel(plotData, outputProtein));
        frame.pack();
        frame.setVisible(true);

        // TODO FIX?
        while (frame.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
    
}