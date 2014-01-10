package dk.dtu.sb.outputformatter;

import java.io.IOException;
import java.util.HashMap;

import dk.dtu.sb.Util;
import dk.dtu.sb.data.PlotPoint;
import dk.dtu.sb.data.SimulationResult;

/**
 * Concrete implementation of {@link AbstractOutputFormatter}. Outputs
 * simulation result as CSV.
 */
public class CSV extends AbstractOutputFormatter {

    protected String delimiter = ",";

    private HashMap<Integer, String> header = new HashMap<Integer, String>();

    public void process(SimulationResult data) {
        try {

            writeHeader(data);

            writeContent(data);

            closeFile();

        } catch (IOException e) {
            Util.log.error("An error occurred", e);
        }
    }

    private void writeHeader(SimulationResult data) throws IOException {
        // Index to ensure consistent lookups (Hashmaps do not guarantee
        // ordering)
        int index = 0;
        writeToFile("time");
        for (String species : data.getSpecies()) {
            writeToFile(delimiter + species);
            header.put(index, species);
            index++;
        }
        writeToFile("\n");
    }

    private void writeContent(SimulationResult data) throws IOException {
        for (PlotPoint plot : data.getPlotPoints()) {
            writeToFile("" + plot.getTime());
            for (int i = 0; i < header.size(); i++) {
                writeToFile(delimiter + plot.getMarkings().get(header.get(i)));
            }
            writeToFile("\n");
        }
    }
}
