package dk.dtu.sb.simulator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
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

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.Plot;
import dk.dtu.sb.algorithm.Algorithm;
import dk.dtu.sb.algorithm.GillespieAlgorithm;
import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.output.Output;

public class Simulator {

	Algorithm algorithm;
	StochasticPetriNet spn;
	Parameters params;

	/**
	 * 
	 * @param spn
	 */
	public Simulator(StochasticPetriNet spn) {
		this.spn = spn;
		this.algorithm = new GillespieAlgorithm();
		this.algorithm.setSPN(this.spn);
		this.params = new Parameters();
	}

	/**
	 * 
	 * @param spn
	 * @param algorithm
	 */
	public Simulator(StochasticPetriNet spn, Algorithm algorithm) {
		this.spn = spn;
		this.algorithm = algorithm;
		this.algorithm.setSPN(this.spn);
		this.params = new Parameters();
	}
	/**
	 * 
	 * @param spn
	 * @param algorithm
	 * @param params
	 */
	public Simulator(StochasticPetriNet spn, Algorithm algorithm, Parameters params) {
		this.spn = spn;
		this.algorithm = algorithm;
		this.algorithm.setSPN(this.spn);
		this.params = params;
	}

	public void setSPN(StochasticPetriNet spn) {
		this.spn = spn;
	}

	public void setParams(Parameters params) {
		this.params = params;
	}

	/**
	 * Simulates using the given stoptime and no of iterations
	 * @param iterations
	 * @param stoptime
	 * @throws InterruptedException 
	 * @throws InvocationTargetException 
	 */
	public void simulate() {

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < params.getIterations(); i++) {
			Util.log.info("Simulation iteration " + i);
			algorithm.run(params.getStoptime());
		}

		long endTime = System.currentTimeMillis();

		Util.log.info("Simulation ended in: "+(endTime-startTime)+"ms");
	}
	
	/**Outputs the simulation results in CSV format to fileURL.
	 * @param fileURL
	 * */
	public void writeCSVFile(String fileURL){
		LinkedList<Plot> p = algorithm.getPlotData();
		if(p.isEmpty()){
			Util.log.error("No data to write");
		}else{
			try {
				File f = new File(fileURL);
				if(!f.exists()){
					f.createNewFile();	
				}
				FileWriter fw = new FileWriter(f.getAbsolutePath());
				BufferedWriter bw = new BufferedWriter(fw);
				//Mappings between index and name
				HashMap<Integer,String> h = new HashMap<Integer,String>();
				int index = 0;
				
				//Write header
				bw.write("time");
				for(String i : p.peekFirst().markings.keySet()){
					bw.write(","+i);
					h.put(index, i);
					index++;
				}
				bw.write("\n");
				//Write Content
				for(Plot pl : p){
					bw.write(String.valueOf(pl.time));
					for(int i = 0; i < h.size(); i++){
						bw.write(","+pl.markings.get(h.get(i)));
					}
					bw.write("\n");
				}
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**Outputs the simulation results to a GUI using JFreeChart*/
	public void displayResultGUI(){
		LinkedList<Plot> p = algorithm.getPlotData();
		if (!p.isEmpty()) {
			XYSeriesCollection dataset = new XYSeriesCollection();

			//For each species
			for(String s : p.peekFirst().markings.keySet()){
				XYSeries series = new XYSeries(s);
				// Run through all plots and add their value
				for(Plot d : p){
					series.add(d.time,d.markings.get(s));
				}
				dataset.addSeries(series);
			}
			final JFreeChart chart = ChartFactory.createXYLineChart("DTU-SB", "time [s]", "Concentration [molecules]", 
					dataset, PlotOrientation.VERTICAL, true, true, true);


			//Draw smooth lines :
			//        chart.getXYPlot().setRenderer(new XYSplineRenderer());

			ChartPanel chartpanel = new ChartPanel(chart);
			chartpanel.setDomainZoomable(true);

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(chartpanel, BorderLayout.CENTER);

			final JList list = new JList(createData(p.peekFirst().markings.keySet()));
			list.setCellRenderer(new CheckListRenderer());
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setBorder(new EmptyBorder(0,3,0,0));
			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					int index = list.locationToIndex(e.getPoint());
					CheckableItem item = (CheckableItem) list.getModel()
							.getElementAt(index);
					item.setSelected(!item.isSelected());
					Rectangle rect = list.getCellBounds(index, index);
					list.repaint(rect);
					boolean isVisible;

					//Initially null
					if(chart.getXYPlot().getRenderer().getSeriesVisible(index) == null){
						chart.getXYPlot().getRenderer().setSeriesVisible(index, true);
					}
					isVisible = chart.getXYPlot().getRenderer().getSeriesVisible(index);
					chart.getXYPlot().getRenderer().setSeriesVisible(index, !isVisible);

				}
			});
			JScrollPane sp = new JScrollPane(list);
			JPanel checkListPanel = new JPanel();
			checkListPanel.setLayout(new BorderLayout());
			checkListPanel.add(sp, BorderLayout.CENTER);
			checkListPanel.add(new JLabel("Show species :  "),BorderLayout.NORTH);
			jPanel.add(checkListPanel, BorderLayout.EAST);

			JFrame frame = new JFrame();
			frame.add(jPanel);
			frame.pack();
			frame.setVisible(true);

			//TODO FIX?
			while(frame.isVisible()){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/** Converts strs to an array of CheckAbleItem
	 * @param strs */
	private CheckableItem[] createData(Set<String> strs) {
		int n = 0;
		CheckableItem[] items = new CheckableItem[strs.size()];
		//TODO Sort strings here
		for (String s : strs) {
			items[n] = new CheckableItem(s);
			n++;
		}
		return items;
	}

	public Output getOutput() {
		return null;
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

/** Represents a checkbox with text*/
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
