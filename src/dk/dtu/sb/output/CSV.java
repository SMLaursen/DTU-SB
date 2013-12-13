package dk.dtu.sb.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import dk.dtu.sb.Util;
import dk.dtu.sb.output.data.PlotPoint;

/**
 *
 */
public class CSV extends AbstractOutput {
    
    /**
     * Outputs the simulation results in CSV format to fileURL.
     */
    public void process() {
        if (graphData.isEmpty()) {
            Util.log.error("No data to write");
        } else {
            try {
                String fileURL = "out.csv";//params.getProperty("OUTPUT_FILENAME", "out.csv");
                File f = new File(fileURL);
                if (!f.exists()) {
                    f.createNewFile();
                }
                FileWriter fw = new FileWriter(f.getAbsolutePath());
                BufferedWriter bw = new BufferedWriter(fw);
                // Mappings between index and name
                HashMap<Integer, String> h = new HashMap<Integer, String>();
                HashMap<String, Float> currValues = new HashMap<String,Float>();
               //Index to ensure consistent lookups (Hashmaps do not guarantee ordering)
                int index = 0;

                // Write header
                bw.write("time");
                for (String i : graphData.peekFirst().getMarkings().keySet()) {
                    bw.write("," + i);
                    h.put(index, i);
                    index++;
                }
                bw.write("\n");
                
                // Write Content
                for (PlotPoint pl : graphData) {
                	currValues.putAll(pl.getMarkings());
                	bw.write(String.valueOf(pl.getTime()));
                    for (int i = 0; i < h.size(); i++) {
                        bw.write("," + currValues.get(h.get(i)));
                    }
                    bw.write("\n");
                }
                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
