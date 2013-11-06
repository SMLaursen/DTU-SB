package dk.dtu.sb.simulator;

import java.lang.reflect.InvocationTargetException;
import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.algorithm.Algorithm;
import dk.dtu.sb.algorithm.GillespieAlgorithm;
import dk.dtu.sb.data.OutputData;
import dk.dtu.sb.data.StochasticPetriNet;

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
	
	/**
	 * 
	 * @return
	 */
	public OutputData getOutputData() {
		return new OutputData(algorithm.getPlotData(),spn.getInitialMarkings(),params.getIterations());
	}

}
