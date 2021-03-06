package dk.dtu.sb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import dk.dtu.sb.compiler.AbstractCompiler;

/**
 * Container of Parameters used in simulation, algorithms and loading of input
 * files.
 */
public class Parameters {

    /**
     * The object holding all the parameters.
     */
    private Properties holder = new Properties();

    /**
     * See {@link #getInputFilename()}.
     */
    public static final String PARAM_INPUT_FILENAME_DEFAULT = "input.xml";
    private static final String PARAM_INPUT_FILENAME = "input.file";

    /**
     * See {@link #getInputParserClassName()}.
     */
    public static final String PARAM_INPUT_PARSER_CLASS_DEFAULT = "dk.dtu.sb.parser.SBMLParser";
    private static final String PARAM_INPUT_PARSER_CLASS = "input.parser";

    /**
     * See {@link #getSimAlgorithmClassName()}.
     */
    public static final String PARAM_SIM_ALGORITHM_CLASS_DEFAULT = "dk.dtu.sb.algorithm.GillespieAlgorithm";
    private static final String PARAM_SIM_ALGORITHM_CLASS = "simulation.algorithm";

    /**
     * See {@link #getSimIterations()}.
     */
    public static final int PARAM_SIM_ITERATIONS_DEFAULT = 4;
    private static final String PARAM_SIM_ITERATIONS = "simulation.iterations";

    /**
     * See {@link #getSimMaxIterTime()}.
     */
    public static final int PARAM_SIM_MAXITERTIME_DEFAULT = 60;
    private static final String PARAM_SIM_MAXITERTIME = "simulation.maxIterTime";

    /**
     * See {@link #getSimNoOfThreads()}.
     */
    public static final int PARAM_SIM_NOOFTHREADS_DEFAULT = Runtime
            .getRuntime().availableProcessors();
    private static final String PARAM_SIM_NOOFTHREADS = "simulation.no_of_thread";
    
    /**
     * See {@link #getSimStoptime()}.
     */
    public static final int PARAM_SIM_STOPTIME_DEFAULT = 5000;
    private static final String PARAM_SIM_STOPTIME = "simulation.stoptime";
    
    /**
     * See {@link #getSimRateMode()}.
     */
    public static final int PARAM_SIM_RATE_MODE_CONSTANT = 0;
    /**
     * See {@link #getSimRateMode()}.
     */
    public static final int PARAM_SIM_RATE_MODE_CUSTOM = 100;
    /**
     * See {@link #getSimRateMode()}.
     */
    public static final int PARAM_SIM_RATE_MODE_DEFAULT = PARAM_SIM_RATE_MODE_CUSTOM;
    private static final String PARAM_SIM_RATE_MODE = "simulation.rate_mode";

    /**
     * See {@link #getOutputResultGUI()}.
     */
    public static final boolean PARAM_OUT_RESULT_GUI_DEFAULT = false;
    private static final String PARAM_OUT_RESULT_GUI = "output.graph_gui";

    /**
     * See {@link #getOutputStepCount()}.
     */
    public static final int PARAM_OUT_STEPCOUNT_DEFAULT = 100;
    private static final String PARAM_OUT_STEPCOUNT = "output.stepcount";

    /**
     * See {@link #getOutputFormatterClassName()}.
     */
    public static final String PARAM_OUT_FORMATTER_CLASS_DEFAULT = "dk.dtu.sb.outputformatter.GraphGUI";
    private static final String PARAM_OUT_FORMATTER_CLASS = "output.formatter";
    
    /**
     * See {@link #getOutputFilename()}.
     */
    public static final String PARAM_OUT_FILENAME_DEFAULT = "output.out";
    private static final String PARAM_OUT_FILENAME = "output.filename";
    
    /**
     * See {@link #getSimThreshold()}.
     */
    public static final double PARAM_SIM_THRESHOLD_DEFAULT = 0.05;
    private static final String PARAM_SIM_THRESHOLD = "simulation.threshold";
    
    /**
     * See {@link #getCompilers()}.
     */
    public static final String[] PARAM_COMPILERS_DEFAULT = new String[0];
    private static final String PARAM_COMPILERS = "compilers";

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
            holder.load(new FileInputStream(filename));
        } catch (IOException e) {
            Util.log.error("An error occured when loading the properties file "
                    + filename + ". Using defaults.");
        }
    }

    private void setDefaults() {
        this.setInputFilename(PARAM_INPUT_FILENAME_DEFAULT);
        this.setInputParserClassName(PARAM_INPUT_PARSER_CLASS_DEFAULT);

        this.setSimAlgorithmClassName(PARAM_SIM_ALGORITHM_CLASS_DEFAULT);
        this.setSimIterations(PARAM_SIM_ITERATIONS_DEFAULT);
        this.setSimStoptime(PARAM_SIM_STOPTIME_DEFAULT);
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
            holder.store(new FileOutputStream(filename), null);
        } catch (Exception e) {
            Util.log.error("An error occurred when saving the parameters.");
        }
    }

    /**
     * The fully qualified class name of the algorithm impl.
     */
    public String getSimAlgorithmClassName() {
        return holder.getProperty(PARAM_SIM_ALGORITHM_CLASS,
                PARAM_SIM_ALGORITHM_CLASS_DEFAULT);
    }

    /**
     * See {@link #getSimAlgorithmClassName()}.
     */
    public void setSimAlgorithmClassName(String className) {
        holder.setProperty(PARAM_SIM_ALGORITHM_CLASS, className);
    }
    
    /**
     * The fully qualified class name of the output formatter impl.
     */
    public String getOutputFormatterClassName() {
        return holder.getProperty(PARAM_OUT_FORMATTER_CLASS,
                PARAM_OUT_FORMATTER_CLASS_DEFAULT);
    }

    /**
     * See {@link #getOutputFormatterClassName()}.
     */
    public void setOutputFormatterClassName(String className) {
        holder.setProperty(PARAM_OUT_FORMATTER_CLASS, className);
    }

    /**
     * The fully qualified class name of the parser impl.
     */
    public String getInputParserClassName() {
        return holder.getProperty(PARAM_INPUT_PARSER_CLASS,
                PARAM_INPUT_PARSER_CLASS_DEFAULT);
    }

    /**
     * See {@link #getInputParserClassName()}.
     */
    public void setInputParserClassName(String className) {
        holder.setProperty(PARAM_INPUT_PARSER_CLASS, className);
    }

    /**
     * The name of the input file to the simulator
     */
    public String getInputFilename() {
        return holder.getProperty(PARAM_INPUT_FILENAME,
                PARAM_INPUT_FILENAME_DEFAULT);
    }

    /**
     * See {@link #getInputFilename()}.
     */
    public void setInputFilename(String filename) {
        holder.setProperty(PARAM_INPUT_FILENAME, filename);
    }

    /**
     * The number of times to run the simulation
     */
    public int getSimIterations() {
        int iterations;
        try {
            iterations = Integer.parseInt(holder.getProperty(
                    PARAM_SIM_ITERATIONS, "" + PARAM_SIM_ITERATIONS_DEFAULT));
        } catch (NumberFormatException e) {
            Util.log.warn(holder.getProperty(PARAM_SIM_ITERATIONS)
                    + " is not a valid integer.");
            iterations = PARAM_SIM_ITERATIONS_DEFAULT;
        }
        return iterations;
    }

    /**
     * See {@link #getSimIterations()}.
     */
    public void setSimIterations(int iterations) {
        holder.setProperty(PARAM_SIM_ITERATIONS, "" + iterations);
    }

    /**
     * The maximum number of threads to run the simulation concurrently.
     */
    public int getSimNoOfThreads() {
        int n;
        try {
            n = Integer.parseInt(holder.getProperty(PARAM_SIM_NOOFTHREADS, ""
                    + PARAM_SIM_NOOFTHREADS_DEFAULT));
            if (n < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(holder.getProperty(PARAM_SIM_NOOFTHREADS)
                    + " is not a valid integer.");
            n = PARAM_SIM_NOOFTHREADS_DEFAULT;
        }
        return n;
    }
    
    /**
     * See {@link #getSimNoOfThreads()}.
     */
    public void setSimNoOfThreads(int n) {
        holder.setProperty(PARAM_SIM_NOOFTHREADS, "" + n);
    }

    /**
     * The maximum number of seconds a thread is allowed to run in simulation.
     * Default is 60 seconds.
     */
    public int getSimMaxIterTime() {
        int seconds;
        try {
            seconds = Integer.parseInt(holder.getProperty(
                    PARAM_SIM_MAXITERTIME, "" + PARAM_SIM_MAXITERTIME_DEFAULT));
            if (seconds < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(holder.getProperty(PARAM_SIM_MAXITERTIME)
                    + " is not a valid integer.");
            seconds = PARAM_SIM_MAXITERTIME_DEFAULT;
        }
        return seconds;
    }
    
    /**
     * See {@link #getSimMaxIterTime()}.
     */
    public void setSimMaxIterTime(int seconds) {
        holder.setProperty(PARAM_SIM_MAXITERTIME, "" + seconds);
    }

    /**
     * The maximum of the last time point.
     */
    public int getSimStoptime() {
        int stoptime;
        try {
            stoptime = Integer.parseInt(holder.getProperty(PARAM_SIM_STOPTIME,
                    "" + PARAM_SIM_STOPTIME_DEFAULT));
        } catch (NumberFormatException e) {
            Util.log.warn(holder.getProperty(PARAM_SIM_STOPTIME)
                    + " is not a valid integer.");
            stoptime = PARAM_SIM_STOPTIME_DEFAULT;
        }
        return stoptime;
    }

    /**
     * See {@link #getSimStoptime()}.
     */
    public void setSimStoptime(int stoptime) {
        holder.setProperty(PARAM_SIM_STOPTIME, "" + stoptime);
    }

    /**
     * Whether to show a graph after simulation
     */
    public boolean getOutputResultGUI() {
        boolean result;
        try {
            result = Boolean.parseBoolean(holder.getProperty(
                    PARAM_OUT_RESULT_GUI, "" + PARAM_OUT_RESULT_GUI_DEFAULT));
        } catch (NumberFormatException e) {
            Util.log.warn(holder.getProperty(PARAM_OUT_RESULT_GUI)
                    + " is not valid.");
            result = PARAM_OUT_RESULT_GUI_DEFAULT;
        }
        return result;
    }

    /**
     * See {@link #getOutputResultGUI()}.
     */
    public void setOutputResultGUI(boolean result) {
        holder.setProperty(PARAM_OUT_RESULT_GUI, "" + result);
    }

    /**
     * The number of data-points for output representation. A value of zero will
     * output all generated data-points.
     */
    public int getOutputStepCount() {
        int stepsize;
        try {
            stepsize = Integer.parseInt(holder.getProperty(PARAM_OUT_STEPCOUNT,
                    "" + PARAM_OUT_STEPCOUNT_DEFAULT));
            if (stepsize < 2) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(holder.getProperty(PARAM_OUT_STEPCOUNT)
                    + " is not a valid integer.");
            stepsize = PARAM_OUT_STEPCOUNT_DEFAULT;
        }
        return stepsize;
    }

    /**
     * See {@link #getOutputStepCount()}.
     */
    public void setOutputStepCount(int steps) {
        holder.setProperty(PARAM_OUT_STEPCOUNT, "" + steps);
    }

    /**
     * Default 0. Limits the output generated from the simulation to only save
     * points which delta tau > threshold. Can greatly reduce the required
     * memory during simulation!
     */
    public double getSimThreshold() {
        double threshold;
        try {
            threshold = Float.parseFloat(holder.getProperty(
                    PARAM_SIM_THRESHOLD, "" + PARAM_SIM_THRESHOLD_DEFAULT));
            if (threshold < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(holder.getProperty(PARAM_SIM_THRESHOLD)
                    + " is not a valid double.");
            threshold = PARAM_SIM_THRESHOLD_DEFAULT;
        }
        return threshold;
    }

    /**
     * See {@link #getSimThreshold()}.
     */
    public void setSimThreshold(double threshold) {
        holder.setProperty(PARAM_SIM_THRESHOLD, "" + threshold);
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
    public int getSimRateMode() {
        int mode;
        try {
            mode = Integer.parseInt(holder.getProperty(PARAM_SIM_RATE_MODE, ""
                    + PARAM_SIM_RATE_MODE_DEFAULT));
            if (mode < PARAM_SIM_RATE_MODE_CONSTANT
                    || mode > PARAM_SIM_RATE_MODE_CUSTOM) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(holder.getProperty(PARAM_SIM_RATE_MODE)
                    + " is not valid.");
            mode = PARAM_SIM_RATE_MODE_DEFAULT;
        }
        return mode;
    }

    /**
     * See {@link #getSimRateMode()}.
     */
    public void setSimRateMode(int mode) {
        if (mode < PARAM_SIM_RATE_MODE_CONSTANT
                || mode > PARAM_SIM_RATE_MODE_CUSTOM) {
            Util.log.warn(mode + " is not valid.");
        } else {
            holder.setProperty(PARAM_SIM_RATE_MODE, "" + mode);
        }
    }

    /**
     * A list of fully qualified names of compilers implementing {@link AbstractCompiler}.
     */
    public String[] getCompilers() {
        String compilersString = holder.getProperty(PARAM_COMPILERS);
        return compilersString != null ? compilersString.split(", ") : PARAM_COMPILERS_DEFAULT;
    }

    /**
     * See {@link #getCompilers()}.
     */
    public void setCompilers(String[] compilers) {
        String compilersString = Arrays.toString(compilers);
        compilersString = compilersString.substring(1, compilersString.length()-1);        
        holder.setProperty(PARAM_COMPILERS, compilersString);
    }
    
    /**
     * The filename of the file to save the output to.
     */
    public String getOutputFilename() {
        return holder.getProperty(PARAM_OUT_FILENAME,
                PARAM_OUT_FILENAME_DEFAULT);
    }

    /**
     * See {@link #getOutputFilename()}.
     */
    public void setOutputFilename(String filename) {
        holder.setProperty(PARAM_OUT_FILENAME, filename);
    }

    /**
     * See {@link #getCustom(String, String)}.
     */
    public String getCustom(String key) {
        return getCustom(key, "");
    }

    /**
     * If the parameter don't have a static field in this class, the parameter
     * can be retrieved with this method. This should mainly be used in custom
     * implementations of parsers, algorithms, etc.
     * 
     * @param key
     *            The parameter.
     * @param value
     *            The default value if the parameter is not present.
     * @return The value of the parameter.
     */
    public String getCustom(String key, String value) {
        return holder.getProperty(key, value);
    }

    /**
     * See {@link #getCustom(String, String)}.
     */
    public void setCustom(String key, String value) {
        holder.setProperty(key, value);
    }
}
