package dk.dtu.sb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import dk.dtu.sb.compiler.Compiler;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;
import dk.dtu.sb.simulator.algorithm.GillespieAlgorithm;
import org.apache.commons.cli.*;
import org.apache.commons.logging.impl.SimpleLog;

public class Main {

    /**
     * 
     */
    private static final String OPT_CPROP = "cprop";
    private static final String OPT_RPROP = "rprop";
    private static final String OPT_DEBUG_LONG = "debug";
    private static final String OPT_DEBUG_SHORT = "d";
    private static final String OPT_FILE_LONG = "file";
    private static final String OPT_FILE_SHORT = "f";
    private static final String OPT_VERBOSITY_LONG = "verbosity";
    private static final String OPT_VERBOSITY_SHORT = "v";
    
    private static final String DESCRIPTION = "DTU-SB v0.1";
    
    private static BufferedReader cli = null;

    public static void main(String[] args) {
        setupCli(args);
    }

    /**
     * 
     * @param args
     */
    private static void setupCli(String[] args) {
        CommandLineParser parser = new BasicParser();

        Options options = new Options();
        options.addOption(OPT_CPROP, false, "Create properties file");
        options.addOption(OPT_RPROP, false, "Read properties file");
        options.addOption(OptionBuilder
                .hasArg()
                .withArgName("FILE")
                .withDescription("SBML file to load")
                .withLongOpt(OPT_FILE_LONG)
                .create(OPT_FILE_SHORT));
        options.addOption(OptionBuilder
                .hasArg()
                .withArgName("LEVEL")
                .withDescription("0: all, 1: trace, 2: debug, 3: info [default], 4: warnings, 5: errors, 6: fatal errors")
                .withLongOpt(OPT_VERBOSITY_LONG)
                .create(OPT_VERBOSITY_SHORT));
        options.addOption(OptionBuilder
                .withDescription("Enable debug logging")
                .withLongOpt(OPT_DEBUG_LONG)
                .create(OPT_DEBUG_SHORT));
        
        if (args.length == 0) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("jar DTU-SB[.bundle].jar", DESCRIPTION, options, "", true);
        } else {
            parseCli(parser, options, args);
        }
    }

    /**
     * 
     * @param parser
     * @param options
     * @param args
     */
    private static void parseCli(CommandLineParser parser, Options options,
            String[] args) {
        try {
            CommandLine line = parser.parse(options, args);
            setVerbosity(line);
            parseLine(line);            
        } catch (ParseException e) {
            System.err.println("Something on the CLI was wrong.");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.err.println("Please enter an integer as verbosity level.");
            System.exit(-1);
        }
    }
    
    /**
     * 
     * @param line
     * @throws NumberFormatException
     */
    private static void setVerbosity(CommandLine line) throws NumberFormatException {
        if (line.hasOption(OPT_VERBOSITY_SHORT)) {
            Util.log.setLevel(Integer.parseInt(line.getOptionValue(OPT_VERBOSITY_SHORT)));
        }            
        if (line.hasOption(OPT_DEBUG_SHORT)) {
            Util.log.setLevel(SimpleLog.LOG_LEVEL_DEBUG);
        }
    }
    
    /**
     * 
     * @param line
     */
    private static void parseLine(CommandLine line) {
        if (line.hasOption(OPT_CPROP)) {
            createPropertiesFile();
        } else if (line.hasOption(OPT_RPROP)) {
            simulate(new Parameters(line.getOptionValue(OPT_RPROP)));            
        } else if (line.hasOption(OPT_FILE_SHORT)) {
            Parameters params = new Parameters();
            params.setProperty(Parameters.PARAM_FILENAME, line.getOptionValue(OPT_FILE_SHORT));
            simulate(params);
        }
    }
    
    /**
     * 
     */
    private static void createPropertiesFile() {
        System.out.println("Follow the instructions to create a new properties file to use for simulation.");
        System.out.println("Default in []");
        System.out.println("Simulation algorithm: Gillespie");
        
        Parameters params = new Parameters();
        
        try {
            String filename = prompt("Enter filename", "sim.props");
            
            params.setProperty(Parameters.PARAM_FILENAME, prompt("Input filename", Parameters.PARAM_FILENAME_DEFAULT));
            params.setProperty(Parameters.PARAM_ALGORITHM, Parameters.PARAM_ALGORITHM_DEFAULT);            
            params.setProperty(Parameters.PARAM_SIM_ITERATIONS, prompt("Number of iterations", ""+Parameters.PARAM_SIM_ITERATIONS_DEFAULT)); 
            params.setProperty(Parameters.PARAM_SIM_STOPTIME, prompt("Stoptime", ""+Parameters.PARAM_SIM_STOPTIME_DEFAULT)); 
            
            params.toFile(filename);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
            System.exit(-1);
        }
    }
    
    /**
     * 
     * @param props
     */
    private static void simulate(Parameters params) {
        try {
            SBMLParser parser = new SBMLParser();
            parser.readFile(params.getProperty(Parameters.PARAM_FILENAME, Parameters.PARAM_FILENAME_DEFAULT));
            Compiler compiler = new Compiler(parser.parse());

            GillespieAlgorithm algorithm = new GillespieAlgorithm();
            Simulator simulator = new Simulator(compiler.compile(), algorithm);
            simulator.simulate();
        } catch (Exception e) {
            Util.log.fatal(e);
        }        
    }
    
    /**
     * 
     * @param text
     * @param def
     * @return
     * @throws IOException
     */
    private static String prompt(String text, String def) throws IOException {
        if (def.trim().length() > 0) {
            text += "[" + def + "]";
        }
        System.out.print(text + ": ");
        
        String input = cli().readLine();
        if (input.trim().length() == 0) {
            input = def;
        }
        return input;
    }
        
    /**
     * 
     * @return
     */
    private static BufferedReader cli() {
        if (cli == null) {
            cli = new BufferedReader(new InputStreamReader(System.in));
        }
        return cli;
    }
}
