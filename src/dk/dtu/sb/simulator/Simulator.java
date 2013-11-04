package dk.dtu.sb.simulator;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.Plot;
import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.output.Output;
import dk.dtu.sb.simulator.algorithm.Algorithm;
import dk.dtu.sb.simulator.algorithm.GillespieAlgorithm;

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

	private int getIterations() {
		int iterations;
		try {
			iterations = Integer.parseInt(this.params.getProperty(Parameters.PARAM_SIM_ITERATIONS, ""+Parameters.PARAM_SIM_ITERATIONS_DEFAULT));
		} catch (NumberFormatException e) {
			iterations = Parameters.PARAM_SIM_ITERATIONS_DEFAULT;
			Util.log.warn("ITERATION specified in the properties file was not a number.");
		}
		return iterations;
	}

	private double getStoptime() {
		double stoptime;
		try {
			stoptime = Double.parseDouble(this.params.getProperty(Parameters.PARAM_SIM_STOPTIME, ""+Parameters.PARAM_SIM_STOPTIME_DEFAULT));
		} catch (NumberFormatException e) {
			stoptime = Parameters.PARAM_SIM_STOPTIME_DEFAULT;
			Util.log.warn("STOPTIME specified in the properties file was not a number.");
		}
		return stoptime;
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

		for (int i = 0; i < getIterations(); i++) {
			algorithm.run(getStoptime());
		}

		long endTime = System.currentTimeMillis();

		Util.log.info("Simulation ended in : "+(endTime-startTime)+"ms");
		displayResults(algorithm.getPlotData());
	}
	
	public void displayResults (LinkedList<Plot> p){
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for(String s : p.peekFirst().markings.keySet()){
			XYSeries series = new XYSeries(s);
			for(Plot d : p){
				series.add(d.time,d.markings.get(s));
			}
			dataset.addSeries(series);
		}
        JFreeChart chart = ChartFactory.createXYLineChart("DTU-SB", "time [s]", "Concentration [molecules]", 
        												   dataset, PlotOrientation.VERTICAL, true, true, true);
 
        //Draw smooth lines :
//        chart.getXYPlot().setRenderer(new XYSplineRenderer());
        
        ChartPanel chartpanel = new ChartPanel(chart);
        chartpanel.setDomainZoomable(true);

        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add(chartpanel, BorderLayout.CENTER);

        JFrame frame = new JFrame();
        frame.add(jPanel4);
        frame.pack();
        frame.setVisible(true);
        
		//TODO FIX / Add action listener
        while(frame.isVisible()){
        	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

	}
	
	public Output getOutput() {
		return null;
	}
}
