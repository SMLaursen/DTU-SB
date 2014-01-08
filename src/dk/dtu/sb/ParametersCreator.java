package dk.dtu.sb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParametersCreator {

    /**
     * Used to ask the user for input.
     */
    private static BufferedReader cli = null;

    /**
     * Dynamically create a properties file by querying the user for parameters.
     */
    public static void createPropertiesFile() {
        System.out
                .println("Follow the instructions to create a new properties file to use for simulation.");
        System.out.println("Default in []");

        Parameters params = new Parameters();

        try {
            params.setInputFilename(prompt("Input filename",
                    Parameters.PARAM_INPUT_FILENAME_DEFAULT));
            params.setInputParserClassName(prompt("Parser class",
                    Parameters.PARAM_INPUT_PARSER_CLASS_DEFAULT));
            params.setSimAlgorithmClassName(prompt("Algorithm class",
                    Parameters.PARAM_SIM_ALGORITHM_CLASS_DEFAULT));
            params.setOutputFormatterClassName(prompt("Output formatter class",
                    Parameters.PARAM_OUT_FORMATTER_CLASS_DEFAULT));
            params.setOutputFilename(prompt("Output filename",
                    Parameters.PARAM_OUT_FILENAME_DEFAULT));
            params.setSimIterations(createIntProperty("Number of iterations",
                    Parameters.PARAM_SIM_ITERATIONS_DEFAULT));
            params.setSimStoptime(createIntProperty("Stoptime",
                    Parameters.PARAM_SIM_ITERATIONS_DEFAULT));
            params.setOutputStepCount(createIntProperty("Output stepsize",
                    Parameters.PARAM_OUT_STEPCOUNT_DEFAULT));
            params.setSimMaxIterTime(createIntProperty("Maximum time to run simulations",
                    Parameters.PARAM_SIM_MAXITERTIME_DEFAULT));
            params.setSimNoOfThreads(createIntProperty("Number of concurrent threads",
                    Parameters.PARAM_SIM_NOOFTHREADS_DEFAULT));
            
            boolean resultGUI = Parameters.PARAM_OUT_RESULT_GUI_DEFAULT;
            if (prompt("Show graph after simulation?", "no").equals("yes")) {
                resultGUI = true;
            }
            params.setOutputResultGUI(resultGUI);
            
            String filename = prompt("Save as", "sim.properties");

            params.saveAsFile(filename);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
        }
    }

    private static int createIntProperty(String prompt, int def)
            throws IOException {
        int value = def;
        try {
            value = Integer.parseInt(prompt(prompt, "" + def));
        } catch (NumberFormatException e) {
        }
        return value;
    }

    /**
     * Interactively query the user for input.
     * 
     * @param text
     *            The query test.
     * @param def
     *            Default if nothing specified.
     * @return The input entered or the default.
     * @throws IOException
     */
    private static String prompt(String text, String def) throws IOException {
        if (def.trim().length() > 0) {
            text += " [" + def + "]";
        }
        System.out.print(text + ": ");

        String input = cli().readLine();
        if (input.trim().length() == 0) {
            input = def;
        }
        return input;
    }

    /**
     * Helper: Create the input stream reader if not already created.
     * 
     * @return See {@link Main#cli}.
     */
    private static BufferedReader cli() {
        if (cli == null) {
            cli = new BufferedReader(new InputStreamReader(System.in));
        }
        return cli;
    }
}
