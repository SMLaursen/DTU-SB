package dk.dtu.sb.simulator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.algorithm.Algorithm;
import dk.dtu.sb.algorithm.GillespieAlgorithm;
import dk.dtu.sb.data.SimulationResult;
import dk.dtu.sb.spn.StochasticPetriNet;

/**
 * This class simulates the given {@link StochasticPetriNet} using the
 * {@link Algorithm} specified.
 */
public class Simulator {

	public static Simulator instance;
	private String algorithmName;
	private Parameters params;
	private StochasticPetriNet spn;
	private long simulationTime = 0;
	private ExecutorService executor;

	/**
	 * This constructor simulates the {@link StochasticPetriNet} using the
	 * default algorithm {@link GillespieAlgorithm}.
	 * 
	 * @param spn
	 *            The input {@link StochasticPetriNet}.
	 */
	public Simulator(StochasticPetriNet spn) {
		this(spn, new Parameters());
	}

	/**
	 * This constructor simulates the {@link StochasticPetriNet} using the
	 * algorithm defined.
	 * 
	 * @param spn
	 *            The input {@link StochasticPetriNet}.
	 * @param algorithmName
	 *            The fully qualified name for the algorithm to use. Should
	 *            extend {@link Algorithm}.
	 */
	public Simulator(StochasticPetriNet spn, String algorithmName) {
		this(spn, new Parameters(), algorithmName);
	}

	/**
	 * This constructor simulates the {@link StochasticPetriNet} using the
	 * algorithm and simulation parameters defined in the {@link Parameters}
	 * object.
	 * 
	 * @param spn
	 *            The input {@link StochasticPetriNet}.
	 * @param params
	 *            The {@link Parameters} with the algorithm specified in
	 *            {@link Parameters#getSimAlgorithmClassName()}.
	 */
	public Simulator(StochasticPetriNet spn, Parameters params) {
		this(spn, params, params.getSimAlgorithmClassName());
	}

	/**
	 * This constructor simulates the {@link StochasticPetriNet} using the
	 * simulator parameters defined in the {@link Parameters} object and the
	 * algorithm defined.
	 * 
	 * @param spn
	 *            The input {@link StochasticPetriNet}.
	 * @param algorithmName
	 *            The fully qualified name for the algorithm to use. Should
	 *            extend {@link Algorithm}.
	 * @param params
	 *            The {@link Parameters} with additional simulator parameters
	 *            specified.
	 */
	public Simulator(StochasticPetriNet spn, Parameters params,
			String algorithmName) {
		this.spn = spn;
		this.algorithmName = algorithmName;
		this.params = params;
		this.executor = Executors.newFixedThreadPool(params.getSimNoOfThreads());
		instance = this;
	}

	/**
	 * Used to set the input {@link StochasticPetriNet} after instantiation.
	 * 
	 * @param spn
	 *            The input {@link StochasticPetriNet}.
	 */
	public void setSPN(StochasticPetriNet spn) {
		this.spn = spn;
	}

	/**
	 * Used to set the {@link Parameters} object after instantiation.
	 * 
	 * @param params
	 *            The {@link Parameters} with additional simulator parameters
	 *            specified.
	 */
	public void setParams(Parameters params) {
		this.params = params;
	}

	/**
	 * Used to set the algorithm to use after instantiation.
	 * 
	 * @param algorithmName
	 *            The fully qualified name for the algorithm to use. Should
	 *            extend {@link Algorithm}.
	 */
	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	/**
	 * Simulates using the given stoptime and no of iterations
	 */
	public void simulate() {
		Algorithm.setInput(spn, params);
		Util.log.debug("Algorithm class: " + algorithmName);

		try {
			long startTime = System.currentTimeMillis();

			startSimulations(algorithmName);

			simulationTime = System.currentTimeMillis() - startTime;
			Util.log.info("Simulation ended in: " + simulationTime + "ms");
		} catch (InterruptedException e) {
			Util.log.error("Something went wrong when simulating: ", e);
		} catch (Exception e) {
			Util.log.error("The algorithm class: " + algorithmName
					+ " could not be found. Using default.");
		}
	}

	private void startSimulations(String algorithmName)
			throws InstantiationException, IllegalAccessException,
			InterruptedException, ClassNotFoundException {

		Class<?> algorithmClass = Class.forName(algorithmName);
		Algorithm worker;

		for (int iteration = 0; iteration < params.getSimIterations(); iteration++) {
			worker = (Algorithm) algorithmClass.newInstance();
			executor.execute(worker);
		}

		executor.shutdown();
		executor.awaitTermination(params.getSimMaxIterTime(), TimeUnit.SECONDS);
		executor.shutdownNow();

		// Wait additionally 5sec to ensure the thread has been shutdown
		// entirely due to timeout
		if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
			Util.log.info("Aborting: Thread timed out but could not be shutdown");
			System.exit(1);
		}

	}

	/** Calling this method will interrupt all currently running threads and abort the simulation*/
	public static void stopSimulation(){
		if(instance.executor != null){
			instance.executor.shutdown();
			try {
				instance.executor.awaitTermination(2, TimeUnit.SECONDS);
				instance.executor.shutdownNow();
				instance.executor.awaitTermination(2, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			} finally{
				Util.log.info("Simulation manually aborted");
			}
		}
	}

	/**
	 * Mainly used for debugging purposes.
	 * 
	 * @return The simulation time in [ms].
	 */
	public long getSimulationTime() {
		if (simulationTime == 0) {
			Util.log.debug("The simulation might not have been run yet.");
		}
		return simulationTime;
	}

	/**
	 * Get the output after simulation has ended.
	 * 
	 * @return The result of the simulation wrapped in an
	 *         {@link SimulationResult} object.
	 */
	public SimulationResult getOutput() {
		if (!executor.isTerminated()) {
			throw new RuntimeException(
					"The result cannot be used before all iterations of the algorithm run has finished.");
		}
		return new SimulationResult(Algorithm.getOutput(), params);
	}

}
