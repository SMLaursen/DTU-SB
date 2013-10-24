package dk.dtu.sb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


import dk.dtu.sb.compiler.Compiler;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.simulator.Simulator;
import dk.dtu.sb.simulator.algorithm.GillespieAlgorithm;
import org.apache.commons.cli.*;

public class Main {

    /**
     * 
     */
    private static final String OPT_CPROP = "cprop";
    private static final String OPT_RPROP = "rprop";
    private static final String OPT_F = "f";
    
    private static final String DESCRIPTION = "DTU-SB v0.1";
    
    /**
     * 
     */
    private static final String PROPS_FILENAME = "INPUT_FILE";
    private static final String PROPS_FILENAME_DEFAULT = "input.sbml";
    private static final String PROPS_ALGORITHM = "ALGORITHM";
    private static final String PROPS_ALGORITHM_DEFAULT = "gillespie";
    private static final String PROPS_RUNS = "RUNS";
    private static final String PROPS_RUNS_DEFAULT = "10";
    
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
                .create(OPT_F));        
        
        if (args.length == 0) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("jar dtu-sb.jar", DESCRIPTION, options, "", true);
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

            if (line.hasOption(OPT_CPROP)) {
                createPropertiesFile();
            } else if (line.hasOption(OPT_RPROP)) {
                
            } else if (line.hasOption(OPT_F)) {
                Properties props = new Properties();
                props.setProperty(PROPS_FILENAME, line.getOptionValue(OPT_F));
                simulate(props);
            } 
        } catch (ParseException e) {
            System.err.println("Something on the CLI was wrong.");
            System.exit(-1);
        }
    }
    
    /**
     * 
     */
    private static void createPropertiesFile() {
        System.out.println("Follow the instructions to create a new properties file to use for simulation.");
        System.out.println("Default in []");
        System.out.println("Simulation algorithm: Gillespie");
        
        Properties prop = new Properties();
        
        try {
            String filename = prompt("Enter filename", "sim.props");
            
            prop.setProperty(PROPS_FILENAME, prompt("Input filename", PROPS_FILENAME_DEFAULT));
            prop.setProperty(PROPS_ALGORITHM, PROPS_ALGORITHM_DEFAULT);            
            prop.setProperty(PROPS_RUNS, prompt("Number of runs", PROPS_RUNS_DEFAULT));            
            
            prop.store(new FileOutputStream(filename), null);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
            System.exit(-1);
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
     */
    private static void simulate(Properties props) {
        try {
            SBMLParser parser = new SBMLParser();
            parser.readFile(props.getProperty(PROPS_FILENAME, PROPS_FILENAME_DEFAULT));
            Compiler compiler = new Compiler(parser.parse());

            GillespieAlgorithm algorithm = new GillespieAlgorithm();
            Simulator simulator = new Simulator(compiler.compile(), algorithm);
            simulator.run();
        } catch (Exception e) {
        }        
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
