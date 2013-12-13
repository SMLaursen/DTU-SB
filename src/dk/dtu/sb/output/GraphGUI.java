package dk.dtu.sb.output;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
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

import dk.dtu.sb.output.data.PlotPoint;

public class GraphGUI extends AbstractOutput {

    public void process() {
        if (!graphData.isEmpty()) {
            XYSeriesCollection dataset = new XYSeriesCollection();
            HashMap<String, XYSeries> graph = new HashMap<String, XYSeries>();
            // Create a XYSeries for each species
            for (String s : graphData.peekFirst().getMarkings().keySet()) {
                graph.put(s, new XYSeries(s));
            }
            // Run through all plots and add their value
            for (PlotPoint d : graphData) {
                for (String s : d.getMarkings().keySet()) {
                    graph.get(s).add(d.getTime(), d.getMarkings().get(s));
                }
            }
            // Add all series
            for (String s : graphData.peekFirst().getMarkings().keySet()) {
                dataset.addSeries(graph.get(s));
            }
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

            final JList list = new JList(
                    createData(graphData.peekFirst().getMarkings().keySet()));
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
            checkListPanel.add(new JLabel("Show species :  "),
                    BorderLayout.NORTH);
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Converts strs to an array of CheckAbleItem
     * 
     * @param strs
     */
    private CheckableItem[] createData(Set<String> strs) {
        int n = 0;
        CheckableItem[] items = new CheckableItem[strs.size()];
        // TODO Sort strings here
        for (String s : strs) {
            items[n] = new CheckableItem(s);
            n++;
        }
        return items;
    }

}

@SuppressWarnings("serial")
class CheckListRenderer extends JCheckBox implements ListCellRenderer {

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