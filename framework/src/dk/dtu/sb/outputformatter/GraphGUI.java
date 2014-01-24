package dk.dtu.sb.outputformatter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import dk.dtu.sb.data.SimulationResult;
import dk.dtu.sb.data.PlotPoint;

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