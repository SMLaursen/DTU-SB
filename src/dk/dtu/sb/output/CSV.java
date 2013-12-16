package dk.dtu.sb.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

    public void process(SimulationResult result) {
        try {
            String fileName = "out.csv";// params.getProperty("OUTPUT_FILENAME",
                                        // "out.csv");

            BufferedWriter writer = getWriter(fileName);

            HashMap<Integer, String> header = new HashMap<Integer, String>();

            // Index to ensure consistent lookups (Hashmaps do not guarantee
            // ordering)
            int index = 0;

            // Write header
            writer.write("time");
            for (String species : result.getSpecies()) {
                writer.write("," + species);
                header.put(index, species);
                index++;
            }
            writer.write("\n");

            // Write content
            for (PlotPoint plot : result.getPlotPoints()) {
                writer.write("" + plot.getTime());
                for (int i = 0; i < header.size(); i++) {
                    writer.write("," + plot.getMarkings().get(header.get(i)));
                }
                writer.write("\n");
            }
            writer.close();

        } catch (IOException e) {
            Util.log.error("An error occurred", e);
        }
    }

    private BufferedWriter getWriter(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsolutePath());
        return new BufferedWriter(fw);
    }
}
