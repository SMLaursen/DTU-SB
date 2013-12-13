package dk.dtu.sb.simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.algorithm.Algorithm;
import dk.dtu.sb.algorithm.GillespieAlgorithm;
import dk.dtu.sb.data.AlgorithmResult;
import dk.dtu.sb.data.SimulationPoint;
import dk.dtu.sb.data.SimulationResult;
import dk.dtu.sb.spn.StochasticPetriNet;

/**
 * This class simulates the given {@link StochasticPetriNet} using the
 * {@link Algorithm} specified.
 */
public class Simulator {

    private String algorithmName;
    private Parameters params;
    private StochasticPetriNet spn;
    private long simulationTime = 0;
    private ExecutorService executor;
    private List<AlgorithmResult> finalResult = new ArrayList<AlgorithmResult>();

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
     *            {@link Parameters#getAlgorithmClassName()}.
     */
    public Simulator(StochasticPetriNet spn, Parameters params) {
        this(spn, params, params.getAlgorithmClassName());
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
        this.executor = Executors.newFixedThreadPool(params.getNoOfThreads());
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

        try {
            long startTime = System.currentTimeMillis();

            Class<?> algorithmClass = Class.forName(algorithmName);
            Algorithm[] worker = new Algorithm[params.getIterations()];

            Util.log.debug("Algorithm class: " + algorithmName);

            for (int iteration = 0; iteration < params.getIterations(); iteration++) {
                worker[iteration] = (Algorithm) algorithmClass.newInstance();
                executor.execute(worker[iteration]);
            }
            executor.shutdown();
            executor.awaitTermination(params.getMaxIterTime(), TimeUnit.SECONDS);
            executor.shutdownNow();

            // Wait additionally 5sec to ensure the thread has been shutdown
            // entirely due to timeout
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                Util.log.info("Aborting: Thread timed out but could not be shutdown");
                System.exit(1);
            }

            simulationTime = System.currentTimeMillis() - startTime;
            Util.log.info("Simulation ended in: " + simulationTime + "ms");
            
            // build output
            for (int iteration = 0; iteration < params.getIterations(); iteration++) {
                finalResult.add(worker[iteration].getOutput());
                worker[iteration] = null;
            }
        } catch (InterruptedException e) {
            Util.log.error("Something went wrong when simulating: ", e);
        } catch (Exception e) {
            Util.log.error("The algorithm class: " + algorithmName
                    + " could not be found. Using default.");
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
     * @return The result of the simulation wrapped in an {@link SimulationResult}
     *         object.
     */
    public SimulationResult getOutput() {
        if (!executor.isTerminated()) {
            throw new RuntimeException(
                    "The result cannot be used before all iterations of the algorithm run has finished.");
        }
        return new SimulationResult(finalResult, params);
    }

}
