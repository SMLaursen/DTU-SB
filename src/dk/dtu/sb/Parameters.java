package dk.dtu.sb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Container of Parameters used in simulation, algorithms and loading of input
 * files.
 */
public class Parameters extends Properties {
    
    private static final String PARAM_FILENAME = "INPUT_FILE";
    public static final String PARAM_FILENAME_DEFAULT = "input.xml";
    

    private static final String PARAM_PARSER = "PARSER";
    public static final String PARAM_PARSER_DEFAULT = "dk.dtu.sb.parser.SBMLParser";
    
    private static final String PARAM_SIM_ALGORITHM = "SIM_ALGORITHM";
    public static final String PARAM_SIM_ALGORITHM_DEFAULT = "dk.dtu.sb.algorithm.GillespieAlgorithm";
    
    private static final String PARAM_SIM_ITERATIONS = "SIM_ITERATIONS";
    public static final int PARAM_SIM_ITERATIONS_DEFAULT = 1;
    
    private static final String PARAM_SIM_STOPTIME = "SIM_STOPTIME";
    public static final double PARAM_SIM_STOPTIME_DEFAULT = 20;
    
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
        InputStream in = getClass().getResourceAsStream(filename);
        try {
            this.load(in);
        } catch (IOException e) {
            Util.log.fatal(e);
        }
    }
    
    private void setDefaults() {
        this.setFilename(PARAM_FILENAME_DEFAULT);
        this.setParserClassName(PARAM_PARSER_DEFAULT);
        
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
        return this.getProperty(PARAM_PARSER, PARAM_PARSER_DEFAULT);
    }
    
    /**
     * @param className The fully qualified class name of the parser impl.
     */
    public void setParserClassName(String className) {
       this.setProperty(PARAM_PARSER, className); 
    }
    
    /**
     * @return The name of the input file to the simulator
     */
    public String getFilename() {
        return this.getProperty(PARAM_FILENAME, PARAM_FILENAME_DEFAULT);
    }
    
    /**
     * @param filename The name of the input file to the simulator
     */
    public void setFilename(String filename) {
        this.setProperty(PARAM_FILENAME, filename);
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
}
