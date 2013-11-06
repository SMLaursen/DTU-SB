package dk.dtu.sb.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import dk.dtu.sb.data.StochasticPetriNet;

/**
 * Extend this Class to create your own parser. The {@link #parse()} method is the only
 * method that needs to be implemented.
 */
public abstract class Parser {

    /**
     * The StochasticPetriNet to modify and return in {@link #parse()}.
     */
    protected StochasticPetriNet spn = new StochasticPetriNet();
    
    /**
     * The input to parse in {@link #parse()}.
     */
    protected String input = "";

    /**
     * Parse the content of the input.
     * 
     * @return A StochasticPetriNet generated from the input.
     */
    public abstract StochasticPetriNet parse();
    
    /**
     * Read a file with input and set the input parameter.
     * 
     * @param filename The input file.
     * @throws IOException 
     * @throws FileNotFoundException
     */
    public void readFile(String filename) throws IOException, FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        setInput(sb.toString());
        
        reader.close();        
    }
    
    /**
     * Set the input directly, can be used as an alternative to the readFile method.
     * 
     * @param input The input string.
     */
    public void setInput(String input) {
        this.input = input;
    }
    
    /**
     * See {@link Parser#input}.
     */
    public String getInput() {
        return input;
    }
}
