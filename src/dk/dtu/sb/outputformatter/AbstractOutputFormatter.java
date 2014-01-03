package dk.dtu.sb.outputformatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.SimulationResult;

/**
 * Implementations of output formatters should extend this class.
 * {@link #process(SimulationResult)} is the only method that needs to be
 * implemented. the method {@link #process(SimulationResult, Parameters)} will
 * automatically call {@link #process(SimulationResult)}, and {@link #params}
 * will be available in this method.
 */
public abstract class AbstractOutputFormatter {

    /**
     * The parameters object available in output formatter implementations. Use
     * {@link #process(SimulationResult, Parameters)} to override the default
     * object.
     */
    protected Parameters params = new Parameters();

    /**
     * 
     */
    private BufferedWriter writer;

    /**
     * This method should process the input data, e.g. by showing a graph or by
     * outputting in a standardised format. This is the only method that output
     * formatters need to implement.
     * 
     * @param data
     *            The result from the simulation.
     */
    public abstract void process(SimulationResult data);

    /**
     * This method can be used if parameters from the parameters object should
     * be used.
     * 
     * @param data
     *            The result from the simulation.
     * @param params
     *            The concrete parameters object.
     */
    public void process(SimulationResult data, Parameters params) {
        this.params = params;
        process(data);
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
     * 
     * @throws IOException
     */
    private void initWriter() throws IOException {
        File file = new File(params.getOutputFilename());
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsolutePath());
        writer = new BufferedWriter(fw);
    }

    /**
     * 
     * @param contents
     * @throws IOException 
     */
    protected void writeToFile(String contents) throws IOException {
        if (writer == null) {
            initWriter();
        }
        writer.write(contents);
    }

    /**
     * 
     */
    protected void closeFile() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                Util.log.error("An error occurred when trying to close the file "
                        + params.getOutputFilename() + " for writing.");
            }
        }
    }
}