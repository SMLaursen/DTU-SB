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
    
    private static final String PARAM_INPUT_FILENAME = "INPUT_FILE";
    public static final String PARAM_INPUT_FILENAME_DEFAULT = "input.xml";    

    private static final String PARAM_INPUT_PARSER = "INPUT_PARSER";
    public static final String PARAM_INPUT_PARSER_DEFAULT = "dk.dtu.sb.parser.SBMLParser";
    
    private static final String PARAM_SIM_ALGORITHM = "SIM_ALGORITHM";
    public static final String PARAM_SIM_ALGORITHM_DEFAULT = "dk.dtu.sb.algorithm.GillespieAlgorithm";
    
    private static final String PARAM_SIM_ITERATIONS = "SIM_ITERATIONS";
    public static final int PARAM_SIM_ITERATIONS_DEFAULT = 1;
    
    private static final String PARAM_SIM_NOOFTHREADS = "SIM_NOOFTHREADS";
    public static final int PARAM_SIM_NOOFTHREADS_DEFAULT = Runtime.getRuntime().availableProcessors();
    
    private static final String PARAM_SIM_STOPTIME = "SIM_STOPTIME";
    public static final double PARAM_SIM_STOPTIME_DEFAULT = 20;
    
    private static final String PARAM_OUT_RESULT_GUI = "OUT_RESULT_GUI";
    public static final boolean PARAM_OUT_RESULT_GUI_DEFAULT = false;
    
    private static final String PARAM_OUT_STEPSIZE = "OUT_STEPSIZE";
    public static final int PARAM_OUT_STEPSIZE_DEFAULT = 1;
    
    
    
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
     * @param filename The name of the properties file with parameters
     */
    public Parameters(String filename) {
        this.setDefaults();
        try {
            this.load(new FileInputStream(filename));
        } catch (IOException e) {
            Util.log.fatal("An error occured when loading the properties file " + filename + ". Using defaults.");
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
     * @param filename The name of the file to save to. Good practice is to
     * use the .propterties file extension.
     */
    public void toFile(String filename) {
        try {
            this.store(new FileOutputStream(filename), null);
        } catch (Exception e) {
            Util.log.fatal(e);
        }
    }
    
    /**
     * @return The fully qualified class name of the algorithm impl.
     */
    public String getAlgorithmClassName() {
        return this.getProperty(PARAM_SIM_ALGORITHM, PARAM_SIM_ALGORITHM_DEFAULT);
    }
    
    /**
     * @param className The fully qualified class name of the algorithm impl.
     */
    public void setAlgorithmClassName(String className) {
        this.setProperty(PARAM_SIM_ALGORITHM, className);
    }
    
    /**
     * @return The fully qualified class name of the parser impl.
     */
    public String getParserClassName() {
        return this.getProperty(PARAM_INPUT_PARSER, PARAM_INPUT_PARSER_DEFAULT);
    }
    
    /**
     * @param className The fully qualified class name of the parser impl.
     */
    public void setParserClassName(String className) {
       this.setProperty(PARAM_INPUT_PARSER, className); 
    }
    
    /**
     * @return The name of the input file to the simulator
     */
    public String getFilename() {
        return this.getProperty(PARAM_INPUT_FILENAME, PARAM_INPUT_FILENAME_DEFAULT);
    }
    
    /**
     * @param filename The name of the input file to the simulator
     */
    public void setFilename(String filename) {
        this.setProperty(PARAM_INPUT_FILENAME, filename);
    }
    
    /**
     * @return The number of times to run the simulation
     */
    public int getIterations() {
        int iterations;
        try {
            iterations = Integer.parseInt(this.getProperty(PARAM_SIM_ITERATIONS, ""+PARAM_SIM_ITERATIONS_DEFAULT));
        } catch (NumberFormatException e) {
            Util.log.warn(PARAM_SIM_ITERATIONS + " is not a valid integer.");
            iterations = PARAM_SIM_ITERATIONS_DEFAULT;
        }
        return iterations;
    }
    
    /**
     * @param iterations The number of times to run the simulation
     */
    public void setIterations(int iterations) {
        this.setProperty(PARAM_SIM_ITERATIONS, ""+iterations);
    }
    
    /**
     * @return The maximum number of threads to run the simulation
     */
    public int getNoOfThreads() {
        int n;
        try {
            n = Integer.parseInt(this.getProperty(PARAM_SIM_NOOFTHREADS, ""+PARAM_SIM_NOOFTHREADS_DEFAULT));
            if(n < 1){
            	throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(PARAM_SIM_NOOFTHREADS + " is not a valid integer.");
            n = PARAM_SIM_NOOFTHREADS_DEFAULT;
        }
        return n;
    }
    
    /**
     * @param n The maximum number of threads to run the simulation
     */
    public void setNoOfThreads(int n) {
        this.setProperty(PARAM_SIM_NOOFTHREADS, ""+n);
    }
    
    
    /**
     * @return
     */
    public double getStoptime() {
        double stoptime;
        try {
            stoptime = Double.parseDouble(this.getProperty(PARAM_SIM_STOPTIME, ""+PARAM_SIM_STOPTIME_DEFAULT));
        } catch (NumberFormatException e) {
            Util.log.warn(PARAM_SIM_STOPTIME + " is not a valid integer.");
            stoptime = PARAM_SIM_STOPTIME_DEFAULT;
        }
        return stoptime;
    }
    
    /**
     * @param stoptime
     */
    public void setStoptime(double stoptime) {
        this.setProperty(PARAM_SIM_STOPTIME, ""+stoptime);
    }
    
    /**
     * @return Whether to show a graph after simulation
     */
    public boolean getResultGUI() {
        boolean result;
        try {
            result = Boolean.parseBoolean(this.getProperty(PARAM_OUT_RESULT_GUI, ""+PARAM_OUT_RESULT_GUI_DEFAULT));
        } catch (NumberFormatException e) {
            Util.log.warn(PARAM_OUT_RESULT_GUI + " is not a valid integer.");
            result = PARAM_OUT_RESULT_GUI_DEFAULT;
        }
        return result;
    }
    
    /**
     * @param result Whether to show a graph after simulation
     */
    public void setResultGUI(boolean result) {
        this.setProperty(PARAM_OUT_RESULT_GUI, ""+result);
    }
   
    /**
     * @return number of steps between each observations for Output-presentation only.
     * */
    public int getOutStepSize() {
        int stepsize;
        try {
            stepsize = Integer.parseInt(this.getProperty(PARAM_OUT_STEPSIZE, ""+PARAM_OUT_STEPSIZE_DEFAULT));
            if(stepsize < 1){
            	throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Util.log.warn(PARAM_OUT_STEPSIZE + " is not a valid integer.");
            stepsize = PARAM_OUT_STEPSIZE_DEFAULT;
        }
        return stepsize;
    }
    
    /**
     * How many steps between each observations for Output-presentation only.
     * @param steps
     */
    public void setOutStepSize(int steps) {
        this.setProperty(PARAM_OUT_STEPSIZE, ""+steps);
    }
}
