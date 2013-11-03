package dk.dtu.sb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Parameters extends Properties {
    
    public static final String PARAM_FILENAME = "INPUT_FILE";
    public static final String PARAM_FILENAME_DEFAULT = "input.sbml";
    public static final String PARAM_ALGORITHM = "ALGORITHM";
    public static final String PARAM_ALGORITHM_DEFAULT = "gillespie";
    public static final String PARAM_PARSER = "PARSER";
    public static final String PARAM_PARSER_DEFAULT = "sbml";
    
    public static final String PARAM_SIM_ITERATIONS = "ITERATIONS";
    public static final int PARAM_SIM_ITERATIONS_DEFAULT = 10;
    
    public static final String PARAM_SIM_STOPTIME = "STOPTIME";
    public static final double PARAM_SIM_STOPTIME_DEFAULT = 20;
    
    public Parameters() {
        this.setDefaults();
    }
    
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
        this.setProperty(PARAM_FILENAME, PARAM_FILENAME_DEFAULT);
        this.setProperty(PARAM_ALGORITHM, PARAM_ALGORITHM_DEFAULT);
        this.setProperty(PARAM_PARSER, PARAM_PARSER_DEFAULT);
        
        this.setProperty(PARAM_SIM_ITERATIONS, ""+PARAM_SIM_ITERATIONS_DEFAULT);
        this.setProperty(PARAM_SIM_STOPTIME, ""+PARAM_SIM_STOPTIME_DEFAULT);
    }
    
    public void toFile(String filename) {
        try {
            this.store(new FileOutputStream(filename), null);
        } catch (Exception e) {
            Util.log.fatal(e);
        }
    }
    
}
