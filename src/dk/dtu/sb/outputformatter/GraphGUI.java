package dk.dtu.sb.outputformatter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
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

public class GraphGUI extends AbstractOutputFormatter {

    public void process(SimulationResult plotData) {

        XYSeriesCollection dataset = getDataSet(plotData);

        final JFreeChart chart = ChartFactory.createXYLineChart("DTU-SB",
                "time [s]", "Concentration [molecules]", dataset,
                PlotOrientation.VERTICAL, true, true, true);

        // Draw smooth lines :
        // chart.getXYPlot().setRenderer(new XYSplineRenderer());

        ChartPanel chartpanel = new ChartPanel(chart);
        chartpanel.setDomainZoomable(true);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(chartpanel, BorderLayout.CENTER);

        final JList list = new JList(createData(plotData.getSpecies()));

        list.setCellRenderer(new CheckListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBorder(new EmptyBorder(0, 3, 0, 0));
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                CheckableItem item = (CheckableItem) list.getModel()
                        .getElementAt(index);
                item.setSelected(!item.isSelected());
                Rectangle rect = list.getCellBounds(index, index);
                list.repaint(rect);
                boolean isVisible;

                // Initially null
                if (chart.getXYPlot().getRenderer().getSeriesVisible(index) == null) {
                    chart.getXYPlot().getRenderer()
                            .setSeriesVisible(index, true);
                }
                isVisible = chart.getXYPlot().getRenderer()
                        .getSeriesVisible(index);
                chart.getXYPlot().getRenderer()
                        .setSeriesVisible(index, !isVisible);

            }
        });
        JScrollPane sp = new JScrollPane(list);
        JPanel checkListPanel = new JPanel();
        checkListPanel.setLayout(new BorderLayout());
        checkListPanel.add(sp, BorderLayout.CENTER);
        checkListPanel.add(new JLabel("Show species:  "), BorderLayout.NORTH);
        jPanel.add(checkListPanel, BorderLayout.EAST);

        JFrame frame = new JFrame();
        frame.add(jPanel);
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

    private XYSeriesCollection getDataSet(SimulationResult plotData) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        HashMap<String, XYSeries> graph = new HashMap<String, XYSeries>();

        // Create a XYSeries for each species
        for (String speciesId : plotData.getSpecies()) {
            graph.put(speciesId, new XYSeries(speciesId));
        }
        // Run through all plots and add their value
        for (PlotPoint plot : plotData.getPlotPoints()) {
            for (String speciesId : plot.getMarkings().keySet()) {
                graph.get(speciesId).add(plot.getTime(),
                        plot.getMarkings().get(speciesId));
            }
        }
        // Add all series
        for (String speciesId : plotData.getSpecies()) {
            dataset.addSeries(graph.get(speciesId));
        }

        return dataset;
    }

    /**
     * Converts strs to an array of CheckAbleItem
     * 
     * @param strings
     */
    private CheckableItem[] createData(Set<String> strings) {
        CheckableItem[] items = new CheckableItem[strings.size()];

        List<String> list = new ArrayList<String>(strings);
        Collections.sort(list);

        int n = 0;
        for (String row : list) {
            items[n] = new CheckableItem(row);
            n++;
        }

        return items;
    }

}

class CheckListRenderer extends JCheckBox implements ListCellRenderer {

    private static final long serialVersionUID = 1L;

    public CheckListRenderer() {
        setBackground(UIManager.getColor("List.textBackground"));
        setForeground(UIManager.getColor("List.textForeground"));
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((CheckableItem) value).isSelected());
        setFont(list.getFont());
        setText(value.toString());
        return this;
    }
}

/**
 * Represents a checkbox with text
 */
class CheckableItem {
    private String str;

    private boolean isSelected;

    public CheckableItem(String str) {
        this.str = str;
        isSelected = true;
    }

    public void setSelected(boolean b) {
        isSelected = b;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String toString() {
        return str;
    }
}