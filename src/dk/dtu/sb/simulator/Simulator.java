package dk.dtu.sb.simulator;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dk.dtu.sb.algorithm.Algorithm;
import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.OutputData;
import dk.dtu.sb.data.StochasticPetriNet;

public class Simulator {
	
	private String algorithmName;
	private Parameters params;
	private StochasticPetriNet spn;
	/**
	 * 
	 * @param spn
	 */
	public Simulator(StochasticPetriNet spn) {
		this.spn = spn;
		this.params = new Parameters();
		this.algorithmName = params.getAlgorithmClassName();
	}

//	/**
//	 * 
//	 * @param spn
//	 * @param algorithm
//	 */
	public Simulator(StochasticPetriNet spn, String algorithm) {
		this.spn = spn;
		this.algorithmName = algorithm;
		this.params = new Parameters();
	}
	
	public Simulator(StochasticPetriNet spn, Parameters p) {
		this.spn = spn;
		this.params = p;
		this.algorithmName = p.getAlgorithmClassName();
	}
//	
//	/**
//	 * 
//	 * @param spn
//	 * @param algorithm
//	 * @param params
//	 */
//	public Simulator(StochasticPetriNet spn, Algorithm algorithm, Parameters params) {
//		this.spn = spn;
//		this.algorithm = algorithm;
//		this.algorithm.setSPN(this.spn);
//		this.params = params;
//	}

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
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws InterruptedException 
	 * @throws InvocationTargetException 
	 */
	public void simulate(){
		
		try {
			long startTime = System.currentTimeMillis();
			ExecutorService executor = Executors.newFixedThreadPool(params.getNoOfThreads());
			Algorithm.initialize(spn, params.getStoptime());
			Class<?> algorithmClass = Class.forName(algorithmName);
			Algorithm worker;
			for (int i = 0; i < params.getIterations(); i++) {
				worker = (Algorithm) algorithmClass.newInstance();
				executor.execute(worker);
			}
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
			executor.shutdownNow();
			long endTime = System.currentTimeMillis();
			Util.log.info("Simulation ended in: "+(endTime-startTime)+"ms");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public OutputData getOutputData() {
		return new OutputData(Util.getPlotData(),spn.getInitialMarkings(),params.getIterations());
	}

}
