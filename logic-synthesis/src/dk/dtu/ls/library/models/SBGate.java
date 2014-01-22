package dk.dtu.ls.library.models;

import java.io.FileNotFoundException;
import java.io.IOException;

import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.spn.StochasticPetriNet;

public class SBGate implements Comparable<SBGate> {

    public int id;
    public String sbmlFile;
    public int cost;
    
    public String[] inputProteins;
    public String[] intermediateProteins;
    public String outputProtein;
    
    public String SOP;
    
    public int stableStateTime;
    
    public SBGate(int id, String sbmlFile, int cost,
            String[] input, String[] intm, String output, String SOP,
            int stableTime) {
        this.id = id;
        this.sbmlFile = "library/" + sbmlFile;
        this.cost = cost;
        this.inputProteins = input;
        this.intermediateProteins = intm;
        this.outputProtein = output;
        this.stableStateTime = stableTime;
    }
    
    public StochasticPetriNet getSPN() {
        if (sbmlFile == null) {
            throw new RuntimeException("");
        }
        StochasticPetriNet spn = null;
        try {
            SBMLParser parser = new SBMLParser();
            parser.readFile(sbmlFile);
            spn = parser.parse();
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            
        }
        return spn;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return id == ((SBGate) other).id;
    }

    @Override
    public int compareTo(SBGate o) {
        if (cost > o.cost) {
            return 1;
        } else if (cost < o.cost) {
            return -1;
        }
        return 0;
    }
}
