package dk.dtu.sb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import dk.dtu.sb.algorithm.Algorithm;
import dk.dtu.sb.algorithm.GillespieAlgorithm;
import dk.dtu.sb.compiler.Compiler;
import dk.dtu.sb.output.GraphGUI;
import dk.dtu.sb.parser.Parser;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;
import org.apache.commons.cli.*;
import org.apache.commons.logging.impl.SimpleLog;

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
    
    private static BufferedReader cli = null;

    public static void main(String[] args) {
        Util.log.setLevel(SimpleLog.LOG_LEVEL_INFO);
        setupCli(args);
    }

    private static void setupCli(String[] args) {
        CommandLineParser parser = new GnuParser();

        Options options = new Options();
        options.addOption(OPT_CPROP, false, "Create properties file");
        options.addOption(OptionBuilder
                .hasArg()
                .withArgName("file")
                .withDescription("Read properties file")
                .create(OPT_RPROP));
        options.addOption(OptionBuilder
                .hasArg()
                .withArgName("file")
                .withDescription("SBML file to load")
                .withLongOpt(OPT_FILE_LONG)
                .create(OPT_FILE_SHORT));
        options.addOption(OptionBuilder
                .hasArg()
                .withArgName("level")
                .withDescription("0: all, 1: trace, 2: debug, 3: info [default], 4: warnings, 5: errors, 6: fatal errors")
                .withLongOpt(OPT_VERBOSITY_LONG)
                .create(OPT_VERBOSITY_SHORT));
        options.addOption(OptionBuilder
                .withDescription("Enable debug logging")
                .withLongOpt(OPT_DEBUG_LONG)
                .create(OPT_DEBUG_SHORT));        
        options.addOption(OptionBuilder
                .withDescription("Show graph GUI after simulation")
                .withLongOpt(OPT_GRAPH_LONG)
                .create(OPT_GRAPH_SHORT));
        
        if (args.length == 0) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar DTU-SB[.bundle].jar", DESCRIPTION, options, "", true);
        } else {
            parseCli(parser, options, args);
        }
    }

    private static void parseCli(CommandLineParser parser, Options options,
            String[] args) {
        try {
            CommandLine line = parser.parse(options, args);
            setVerbosity(line);
            parseLine(line);            
        } catch (ParseException e) {
            System.err.println("Something on the CLI was wrong: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Please enter an integer as verbosity level: " + e);
        }
    }

    private static void setVerbosity(CommandLine line) throws NumberFormatException {
        if (line.hasOption(OPT_VERBOSITY_SHORT)) {
            Util.log.setLevel(Integer.parseInt(line.getOptionValue(OPT_VERBOSITY_SHORT)));
        }            
        if (line.hasOption(OPT_DEBUG_SHORT)) {
            Util.log.setLevel(SimpleLog.LOG_LEVEL_DEBUG);
        }
    }

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
    
    private static void createPropertiesFile() {
        System.out.println("Follow the instructions to create a new properties file to use for simulation.");
        System.out.println("Default in []");
        
        Parameters params = new Parameters();
        
        try {            
            params.setFilename(prompt("Input filename", Parameters.PARAM_INPUT_FILENAME_DEFAULT));
            params.setParserClassName(prompt("Parser class", Parameters.PARAM_INPUT_PARSER_DEFAULT));
            params.setAlgorithmClassName(prompt("Algorithm class", Parameters.PARAM_SIM_ALGORITHM_DEFAULT));  
            
            int iterations = Parameters.PARAM_SIM_ITERATIONS_DEFAULT;
            try {
                iterations = Integer.parseInt(prompt("Number of iterations", ""+Parameters.PARAM_SIM_ITERATIONS_DEFAULT));
            } catch (NumberFormatException e) {
            }
            params.setIterations(iterations); 
            
            double stoptime = Parameters.PARAM_SIM_ITERATIONS_DEFAULT;
            try {
                stoptime = Double.parseDouble(prompt("Stoptime", ""+Parameters.PARAM_SIM_STOPTIME_DEFAULT));
            } catch (NumberFormatException e) {
            }
            params.setStoptime(stoptime);
            
            boolean resultGUI = Parameters.PARAM_SIM_RESULT_GUI_DEFAULT;
            if (prompt("Show graph after simulation?", "no").equals("yes")) {
                resultGUI = true;
            }
            params.setResultGUI(resultGUI);
            
            String filename = prompt("Save as", "sim.properties");
            
            params.toFile(filename);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
        }
    }

    private static void simulate(Parameters params) {
        String filename = params.getFilename();
        try {
            // instantiate parser specified in parameters file
            Parser parser = getParser(params.getParserClassName());  
            parser.readFile(filename);
            
            // instantiate compiler
            Compiler compiler = new Compiler(parser.parse());
            
            // instantiate algorithm specified in parameters file
            Algorithm algorithm = getAlgorithm(params.getAlgorithmClassName());
            
            // input result of compilation and algorithm to the simulator and run
            Simulator simulator = new Simulator(compiler.compile(), algorithm);
            simulator.simulate();
            
            // show graph
            if (params.getResultGUI()) {
                GraphGUI gui = new GraphGUI();
                gui.setData(simulator.getOutputData());
                gui.process();
            }
        } catch (FileNotFoundException e) {
            Util.log.fatal("Input file: " + filename + " was not found.");
        } catch (Exception e) {
            Util.log.fatal("Simulation error:", e);
        }
    }

    private static Algorithm getAlgorithm(String className) {
        try {
            Class<?> algorithmClass = Class.forName(className);
            Algorithm algorithm = (Algorithm) algorithmClass.newInstance();
            
            Util.log.debug("Algorithm class: " + className);
            
            return algorithm;
        } catch (ClassNotFoundException e) {
            Util.log.fatal("The algorithm class: " + className + " could not be found. Using default.");
        } catch (Exception e) {
            Util.log.fatal("Using default algorithm.", e);
        }
        return new GillespieAlgorithm();
    }

    private static Parser getParser(String className) {
        try {
            Class<?> parserClass = Class.forName(className);
            Parser parser = (Parser) parserClass.newInstance();
            
            Util.log.debug("Parser class: " + className);
            
            return parser;
        } catch (ClassNotFoundException e) {
            Util.log.fatal("The parser class: " + className + " could not be found. Using default.");
        } catch (Exception e) {
            Util.log.fatal("Using default parser.", e);
        }
        return new SBMLParser();
    }

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

    private static BufferedReader cli() {
        if (cli == null) {
            cli = new BufferedReader(new InputStreamReader(System.in));
        }
        return cli;
    }
}
