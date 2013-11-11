package dk.dtu.sb.simulator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.algorithm.Algorithm;
import dk.dtu.sb.data.OutputData;
import dk.dtu.sb.data.StochasticPetriNet;

public class Simulator {

    private String algorithmName;
    private Parameters params;
    private StochasticPetriNet spn;
    private long simulationTime = 0;

    /**
     * 
     * @param spn
     */
    public Simulator(StochasticPetriNet spn) {
        this.spn = spn;
        this.params = new Parameters();
        this.algorithmName = this.params.getAlgorithmClassName();
    }

    /**
     * 
     * @param spn
     * @param algorithm
     */
    public Simulator(StochasticPetriNet spn, String algorithmName) {
        this.spn = spn;
        this.algorithmName = algorithmName;
        this.params = new Parameters();
    }

    /**
     * 
     * @param spn
     * @param p
     */
    public Simulator(StochasticPetriNet spn, Parameters p) {
        this.spn = spn;
        this.params = p;
        this.algorithmName = this.params.getAlgorithmClassName();
    }

    /**
     * 
     * @param spn
     * @param algorithm
     * @param params
     */
    public Simulator(StochasticPetriNet spn, String algorithmName,
            Parameters params) {
        this.spn = spn;
        this.algorithmName = algorithmName;
        this.params = params;
    }

    /**
     * 
     * @param spn
     */
    public void setSPN(StochasticPetriNet spn) {
        this.spn = spn;
    }

    /**
     * 
     * @param params
     */
    public void setParams(Parameters params) {
        this.params = params;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    /**
     * Simulates using the given stoptime and no of iterations
     */
    public void simulate() {
        Algorithm.setInput(spn, params.getStoptime());

        try {
            long startTime = System.currentTimeMillis();

            ExecutorService executor = Executors.newFixedThreadPool(params.getNoOfThreads());

            Class<?> algorithmClass = Class.forName(algorithmName);
            Algorithm worker;

            Util.log.debug("Algorithm class: " + algorithmName);

            for (int i = 0; i < params.getIterations(); i++) {
                worker = (Algorithm) algorithmClass.newInstance();
                executor.execute(worker);
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            executor.shutdownNow();

            simulationTime = System.currentTimeMillis() - startTime;
            Util.log.info("Simulation ended in: " + simulationTime + "ms");

        } catch (InterruptedException e) {
            Util.log.fatal("Something went wrong when simulating: ", e);
        } catch (Exception e) {
            Util.log.fatal("The algorithm class: " + algorithmName
                    + " could not be found. Using default.");
        }
    }

    /**
     * Mainly used for debugging purposes
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
     * 
     * @return
     */
    public OutputData getOutputData() {
        return new OutputData(Algorithm.getOutput(), spn.getInitialMarkings(), params.getIterations(), params.getStoptime());
    }

}
