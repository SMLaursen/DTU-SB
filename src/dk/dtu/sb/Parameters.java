package dk.dtu.sb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Container of Parameters used in simulation, algorithms and loading of input
 * files.
 */
public class Parameters extends Properties {

    private static final String PARAM_INPUT_FILENAME = "input.file";
    public static final String PARAM_INPUT_FILENAME_DEFAULT = "input.xml";

    private static final String PARAM_INPUT_PARSER = "input.parser";
    public static final String PARAM_INPUT_PARSER_DEFAULT = "dk.dtu.sb.parser.SBMLParser";

    private static final String PARAM_SIM_ALGORITHM = "simulation.algorithm";
    public static final String PARAM_SIM_ALGORITHM_DEFAULT = "dk.dtu.sb.algorithm.GillespieAlgorithm";

    private static final String PARAM_SIM_ITERATIONS = "simulation.iterations";
    public static final int PARAM_SIM_ITERATIONS_DEFAULT = 1;

    private static final String PARAM_SIM_MAXITERTIME = "simulation.maxIterTime";
    public static final int PARAM_SIM_MAXITERTIME_DEFAULT = 60;

    private static final String PARAM_SIM_NOOFTHREADS = "simulation.no_of_thread";
    public static final int PARAM_SIM_NOOFTHREADS_DEFAULT = Runtime
            .getRuntime().availableProcessors();

    private static final String PARAM_SIM_STOPTIME = "simulation.stoptime";
    public static final int PARAM_SIM_STOPTIME_DEFAULT = 20;

    private static final String PARAM_SIM_RATE_MODE = "simulation.rate_mode";
    public static final int PARAM_SIM_RATE_MODE_CONSTANT = 0;
    public static final int PARAM_SIM_RATE_MODE_CUSTOM = 100;
    public static final int PARAM_SIM_RATE_MODE_DEFAULT = PARAM_SIM_RATE_MODE_CUSTOM;

    private static final String PARAM_OUT_RESULT_GUI = "output.graph_gui";
    public static final boolean PARAM_OUT_RESULT_GUI_DEFAULT = false;

    private static final String PARAM_OUT_STEPCOUNT = "output.stepcount";
    public static final int PARAM_OUT_STEPCOUNT_DEFAULT = 1000;
    
    private static final String PARAM_SIM_THRESHOLD = "simulation.threshold";
    public static final double PARAM_SIM_THRESHOLD_DEFAULT = 0.0;

    /**
     * Instantiates a Parameter object with default values.
     */
    public Parameters() {
        this.setDefaults();
    }

    /**
     * Instantiates a Parameter object with values from a specified file.
     * Missing values will get the default value
     * 
     * @param filename
     *            The name of the properties file with parameters
     */
    public Parameters(String filename) {
        this.setDefaults();
        try {
            this.load(new FileInputStream(filename));
        } catch (IOException e) {
            Util.log.fatal("An error occured when loading the properties file "
                    + filename + ". Using defaults.");
        }
    }

    private void setDefaults() {
        this.setFilename(PARAM_INPUT_FILENAME_DEFAULT);
        this.setParserClassName(PARAM_INPUT_PARSER_DEFAULT);

        this.setAlgorithmClassName(PARAM_SIM_ALGORITHM_DEFAULT);
        this.setIterations(PARAM_SIM_ITERATIONS_DEFAULT);
        this.setStoptime(PARAM_SIM_STOPTIME_DEFAULT);
    }

    /**
     * Save the current parameters to a file.
     * 
     * @param filename
     *            The name of the file to save to. Good practice is to use the
     *            .propterties file extension.
     */
    public void saveAsFile(String filename) {
        try {
            this.store(new FileOutputStream(filename), null);
        } catch (Exception e) {
            Util.log.fatal(e);
        }
    }

    /**
     * The fully qualified class name of the algorithm impl.
     */
    public String getAlgorithmClassName() {
        return this.getProperty(PARAM_SIM_ALGORITHM,
                PARAM_SIM_ALGORITHM_DEFAULT);
    }

    /**
     * See {@link #getAlgorithmClassName()}.
     */
    public void setAlgorithmClassName(String className) {
        this.setProperty(PARAM_SIM_ALGORITHM, className);
    }

    /**
     * The fully qualified class name of the parser impl.
     */
    public String getParserClassName() {
        return this.getProperty(PARAM_INPUT_PARSER, PARAM_INPUT_PARSER_DEFAULT);
    }

    /**
     * See {@link #getParserClassName()}.
     */
    public void setParserClassName(String className) {
        this.setProperty(PARAM_INPUT_PARSER, className);
    }

    /**
     * The name of the input file to the simulator
     */
    public String getFilename() {
        return this.getProperty(PARAM_INPUT_FILENAME,
                PARAM_INPUT_FILENAME_DEFAULT);
    }

    /**
     * See {@link #getFilename()}.
     */
    public void setFilename(String filename) {
        this.setProperty(PARAM_INPUT_FILENAME, filename);
    }

    /**
     * The number of times to run the simulation
     */
    public int getIterations() {
        int iterations;
        try {
            iterations = Integer.parseInt(this.getProperty(
                    PARAM_SIM_ITERATIONS, "" + PARAM_SIM_ITERATIONS_DEFAULT));
        } catch (NumberFormatException e) {
            Util.log.warn(this.getProperty(PARAM_SIM_ITERATIONS)
                    + " is not a valid integer.");
            iterations = PARAM_SIM_ITERATIONS_DEFAULT;
        }
        return iterations;
    }
    
    /**
     * See {@link #getIterations()}.
     */
    public void setIterations(int iterations) {
        this.setProperty(PARAM_SIM_ITERATIONS, "" + iterations);
    }

    /**
     * The maximum number of threads to run the simulation concurrently.
     */
    public int getNoOfThreads() {
        int n;
        try {
            n = Integer.parseInt(this.getProperty(PARAM_SIM_NOOFTHREADS, ""
                    + PARAM_SIM_NOOFTHREADS_DEFAULT));
            if (n < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(this.getProperty(PARAM_SIM_NOOFTHREADS)
                    + " is not a valid integer.");
            n = PARAM_SIM_NOOFTHREADS_DEFAULT;
        }
        return n;
    }
    
    /**
     * See {@link #getIterations()}.
     */
    public void setMaxIterTime(int seconds) {
        this.setProperty(PARAM_SIM_MAXITERTIME, "" + seconds);
    }

    /**
     * The maximum number of seconds a thread is allowed to run in simulation. Default is 60 seconds.
     */
    public int getMaxIterTime() {
        int seconds;
        try {
            seconds = Integer.parseInt(this.getProperty(PARAM_SIM_MAXITERTIME, ""
                    + PARAM_SIM_MAXITERTIME_DEFAULT));
            if (seconds < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(this.getProperty(PARAM_SIM_MAXITERTIME)
                    + " is not a valid integer.");
            seconds = PARAM_SIM_MAXITERTIME_DEFAULT;
        }
        return seconds;
    }

    /**
     * See {@link #getNoOfThreads()}.
     */
    public void setNoOfThreads(int n) {
        this.setProperty(PARAM_SIM_NOOFTHREADS, "" + n);
    }

    /**
     * The maximum of the last time point.
     */
    public int getStoptime() {
        int stoptime;
        try {
            stoptime = Integer.parseInt(this.getProperty(PARAM_SIM_STOPTIME, ""
                    + PARAM_SIM_STOPTIME_DEFAULT));
        } catch (NumberFormatException e) {
            Util.log.warn(this.getProperty(PARAM_SIM_STOPTIME)
                    + " is not a valid integer.");
            stoptime = PARAM_SIM_STOPTIME_DEFAULT;
        }
        return stoptime;
    }

    /**
     * See {@link #getStoptime()}.
     */
    public void setStoptime(int stoptime) {
        this.setProperty(PARAM_SIM_STOPTIME, "" + stoptime);
    }

    /**
     * Whether to show a graph after simulation
     */
    public boolean getResultGUI() {
        boolean result;
        try {
            result = Boolean.parseBoolean(this.getProperty(
                    PARAM_OUT_RESULT_GUI, "" + PARAM_OUT_RESULT_GUI_DEFAULT));
        } catch (NumberFormatException e) {
            Util.log.warn(this.getProperty(PARAM_OUT_RESULT_GUI)
                    + " is not valid.");
            result = PARAM_OUT_RESULT_GUI_DEFAULT;
        }
        return result;
    }

    /**
     * See {@link #getResultGUI()}.
     */
    public void setResultGUI(boolean result) {
        this.setProperty(PARAM_OUT_RESULT_GUI, "" + result);
    }

    /**
     * The number of data-points for output representation. A value of zero will
     * output all generated data-points.
     */
    public int getOutStepCount() {
        int stepsize;
        try {
            stepsize = Integer.parseInt(this.getProperty(PARAM_OUT_STEPCOUNT,
                    "" + PARAM_OUT_STEPCOUNT_DEFAULT));
            if (stepsize < 2) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(this.getProperty(PARAM_OUT_STEPCOUNT)
                    + " is not a valid integer.");
            stepsize = PARAM_OUT_STEPCOUNT_DEFAULT;
        }
        return stepsize;
    }

    /**
     * See {@link #getOutStepCount()}.
     */
    public void setOutStepCount(int steps) {
        this.setProperty(PARAM_OUT_STEPCOUNT, "" + steps);
    }
    
    
    /**
     * Default 0. Limits the output generated from the simulation to only save points which 
     * delta tau > threshold. Can greatly reduce the required memory!
     */
    public double getSimThreshold() {
        double threshold;
        try {
            threshold = Float.parseFloat(this.getProperty(PARAM_SIM_THRESHOLD,
                    "" + PARAM_SIM_THRESHOLD_DEFAULT));
            if (threshold < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(this.getProperty(PARAM_SIM_THRESHOLD)
                    + " is not a valid double.");
            threshold = PARAM_SIM_THRESHOLD_DEFAULT;
        }
        return threshold;
    }

    /**
     * See {@link #getOutStepCount()}.
     */
    public void setSimThreshold(double threshold) {
        this.setProperty(PARAM_SIM_THRESHOLD, "" + threshold);
    }

    /**
     * How the algorithms should calculate rates. Currently two modes are
     * supported:
     * <ul>
     * <li><b>Constant rate ({@link #PARAM_SIM_RATE_MODE_CONSTANT}):</b> The
     * rate is constant, and the reactants of a reaction is parsed to the
     * binomial function, ensuring that the rate will change depending on the
     * amount of available reactants.</li>
     * <li><b>Custom function rate ({@link #PARAM_SIM_RATE_MODE_CUSTOM}):</b> A
     * valid mathematical expression representing the rate of a reaction. This
     * will typically depend on one or more species in the system.</li>
     * </ul>
     */
    public int getRateMode() {
        int mode;
        try {
            mode = Integer.parseInt(this.getProperty(PARAM_SIM_RATE_MODE, ""
                    + PARAM_SIM_RATE_MODE_DEFAULT));
            if (mode < PARAM_SIM_RATE_MODE_CONSTANT
                    || mode > PARAM_SIM_RATE_MODE_CUSTOM) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(this.getProperty(PARAM_SIM_RATE_MODE)
                    + " is not valid.");
            mode = PARAM_SIM_RATE_MODE_DEFAULT;
        }
        return mode;
    }

    /**
     * See {@link #getRateMode()}.
     */
    public void setRateMode(int mode) {
        if (mode < PARAM_SIM_RATE_MODE_CONSTANT
                || mode > PARAM_SIM_RATE_MODE_CUSTOM) {
            Util.log.warn(mode + " is not valid.");
        } else {
            this.setProperty(PARAM_SIM_RATE_MODE, "" + mode);
        }
    }
}
