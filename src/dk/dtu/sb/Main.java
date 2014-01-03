package dk.dtu.sb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import dk.dtu.sb.compiler.Compiler;
import dk.dtu.sb.output.GraphGUI;
import dk.dtu.sb.parser.AbstractParser;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;

import org.apache.commons.cli.*;

import ch.qos.logback.classic.Level;

/**
 * Main-class used for CLI-usage of the framework.
 */
public class Main {

    private static final String OPT_CPROP = "cprop";
    private static final String OPT_RPROP = "rprop";

    private static final String OPT_DEBUG_LONG = "debug";
    private static final String OPT_DEBUG_SHORT = "d";

    private static final String OPT_FILE_LONG = "file";
    private static final String OPT_FILE_SHORT = "f";

    private static final String OPT_VERBOSITY_LONG = "verbosity";
    private static final String OPT_VERBOSITY_SHORT = "v";

    private static final String OPT_GRAPH_LONG = "graph";
    private static final String OPT_GRAPH_SHORT = "g";

    private static final String DESCRIPTION = "DTU-SB v0.1";

    /**
     * Used to ask the user for input.
     */
    private static BufferedReader cli = null;

    /**
     * Initiates the simulation. Resets logging level.
     * 
     * @param args
     *            From command line.
     */
    public static void main(String[] args) {
        Util.log.setLevel(Level.INFO);
        setupCli(args);
    }

    // https://github.com/NanoHttpd/nanohttpd

    /**
     * Uses Apache Commons CLI to setup accepted parameters on the CLI.
     * 
     * @param args
     *            From command line.
     */
    private static void setupCli(String[] args) {
        CommandLineParser parser = new GnuParser();

        Options options = new Options();
        options.addOption(OPT_CPROP, false, "Create properties file");
        options.addOption(OptionBuilder.hasArg().withArgName("file")
                .withDescription("Read properties file").create(OPT_RPROP));
        options.addOption(OptionBuilder.hasArg().withArgName("file")
                .withDescription("SBML file to load")
                .withLongOpt(OPT_FILE_LONG).create(OPT_FILE_SHORT));
        options.addOption(OptionBuilder
                .hasArg()
                .withArgName("level")
                .withDescription(
                        "0: all, 1: trace, 2: debug, 3: info [default], 4: warnings, 5: errors")
                .withLongOpt(OPT_VERBOSITY_LONG).create(OPT_VERBOSITY_SHORT));
        options.addOption(OptionBuilder.withDescription("Enable debug logging")
                .withLongOpt(OPT_DEBUG_LONG).create(OPT_DEBUG_SHORT));
        options.addOption(OptionBuilder
                .withDescription("Show graph GUI after simulation")
                .withLongOpt(OPT_GRAPH_LONG).create(OPT_GRAPH_SHORT));

        if (args.length == 0) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar DTU-SB[.bundle].jar", DESCRIPTION,
                    options, "", true);
        } else {
            parseCli(parser, options, args);
        }
    }

    /**
     * Sets the verbosity and initiates parsing.
     * 
     * @param parser
     *            The parser used.
     * @param options
     *            Available parameters.
     * @param args
     *            From command line.
     */
    private static void parseCli(CommandLineParser parser, Options options,
            String[] args) {
        try {
            CommandLine line = parser.parse(options, args);
            setVerbosity(line);
            parseLine(line);
        } catch (ParseException e) {
            System.err.println("Something on the CLI was wrong: "
                    + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Please enter an integer as verbosity level: "
                    + e);
        }
    }

    /**
     * Sets the verbosity based on the input.
     * 
     * @param line
     *            The parsed CLI.
     * @throws NumberFormatException
     */
    private static void setVerbosity(CommandLine line)
            throws NumberFormatException {
        if (line.hasOption(OPT_VERBOSITY_SHORT)) {
            int levelNum = (Integer.parseInt(line
                    .getOptionValue(OPT_VERBOSITY_SHORT)) - 1) * 10000;
            Level level = Level.ALL;
            level = (levelNum == 0) ? Level.TRACE : Level.toLevel(levelNum);
            Util.log.setLevel(level);
        }
        if (line.hasOption(OPT_DEBUG_SHORT)) {
            Util.log.setLevel(Level.DEBUG);
        }
    }

    /**
     * Start simulation based on the information given.
     * 
     * @param line
     *            The parsed CLI.
     */
    private static void parseLine(CommandLine line) {
        if (line.hasOption(OPT_CPROP)) {
            createPropertiesFile();
        } else {
            if (line.hasOption(OPT_RPROP)) {
                simulate(new Parameters(line.getOptionValue(OPT_RPROP)));
            } else if (line.hasOption(OPT_FILE_SHORT)) {
                Parameters params = new Parameters();
                params.setFilename(line.getOptionValue(OPT_FILE_SHORT));

                if (line.hasOption(OPT_GRAPH_SHORT)) {
                    params.setResultGUI(true);
                }

                simulate(params);
            } else {
                Util.log.info("You have to provide either a properties file as input or specify a file as input.");
            }
        }
    }

    /**
     * Dynamically create a properties file by querying the user for parameters.
     */
    private static void createPropertiesFile() {
        System.out
                .println("Follow the instructions to create a new properties file to use for simulation.");
        System.out.println("Default in []");

        Parameters params = new Parameters();

        try {
            params.setFilename(prompt("Input filename",
                    Parameters.PARAM_INPUT_FILENAME_DEFAULT));
            params.setParserClassName(prompt("Parser class",
                    Parameters.PARAM_INPUT_PARSER_CLASS_DEFAULT));
            params.setAlgorithmClassName(prompt("Algorithm class",
                    Parameters.PARAM_SIM_ALGORITHM_CLASS_DEFAULT));

            int iterations = createIntProperty("Number of iterations", Parameters.PARAM_SIM_ITERATIONS_DEFAULT);
            params.setIterations(iterations);

            int stoptime = createIntProperty("Stoptime", Parameters.PARAM_SIM_ITERATIONS_DEFAULT);
            params.setStoptime(stoptime);

            boolean resultGUI = Parameters.PARAM_OUT_RESULT_GUI_DEFAULT;
            if (prompt("Show graph after simulation?", "no").equals("yes")) {
                resultGUI = true;
            }
            params.setResultGUI(resultGUI);

            int stepsize = createIntProperty("Output stepsize", Parameters.PARAM_OUT_STEPCOUNT_DEFAULT);
            params.setOutStepCount(stepsize);

            String filename = prompt("Save as", "sim.properties");

            params.saveAsFile(filename);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
        }
    }

    private static int createIntProperty(String prompt, int def) throws IOException {
        int value = def;
        try {
            value = Integer.parseInt(prompt(prompt, "" + def));
        } catch (NumberFormatException e) {
        }
        return value;
    }

    /**
     * Set up the the tool chain required for simulation and run the simulation
     * 
     * @param params
     *            The parameters determined by user input. Either a default
     *            {@link Parameters} object with filename changed or a custom
     *            object.
     */
    private static void simulate(Parameters params) {
        String filename = params.getFilename();
        try {
            // instantiate parser specified in parameters file
            AbstractParser abstractParser = getParser(params.getParserClassName());
            abstractParser.readFile(filename);

            // instantiate compiler
            Compiler compiler = new Compiler(abstractParser.parse(), params);

            // input result of compilation and algorithm to the simulator and
            // run
            Simulator simulator = new Simulator(compiler.compile(), params);
            simulator.simulate();

            // show graph
            if (params.getResultGUI()) {
                GraphGUI gui = new GraphGUI();
                gui.process(simulator.getOutput(), params);
            }
        } catch (FileNotFoundException e) {
            Util.log.error("Input file: " + filename + " was not found.");
        } catch (IOException e) {
            Util.log.error("An error occurred when reading the content of "
                    + filename);
        }
    }

    /**
     * Creates an instance of the parser specified. Fallback to default
     * {@link SBMLParser} if the parser specified could not be found.
     * 
     * @param className
     *            The fully qualified name of the parser extending
     *            {@link AbstractParser}.
     * @return An instance of the Parser specified.
     */
    private static AbstractParser getParser(String className) {
        try {
            Class<?> parserClass = Class.forName(className);
            AbstractParser abstractParser = (AbstractParser) parserClass.newInstance();

            Util.log.debug("Parser class: " + className);

            return abstractParser;
        } catch (ClassNotFoundException e) {
            Util.log.error("The parser class: " + className
                    + " could not be found. Using default.");
        } catch (Exception e) {
            Util.log.error("Using default parser.", e);
        }
        return new SBMLParser();
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
