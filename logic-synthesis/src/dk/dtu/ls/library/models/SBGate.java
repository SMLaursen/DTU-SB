package dk.dtu.ls.library.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import dk.dtu.sb.Util;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.spn.Reaction;
import dk.dtu.sb.spn.Species;
import dk.dtu.sb.spn.StochasticPetriNet;

public class SBGate implements Comparable<SBGate> {
    
    public static final int LOW = 0;
    public static final int HIGH = 100;
    
    public static SBGate compose(SBGate gate1, SBGate gate2) {
        SBGate newGate = new SBGate(gate1.hashCode() + gate2.hashCode(), gate1.cost + gate2.cost);
        
        StochasticPetriNet result = gate1.getSPN().clone();
        StochasticPetriNet spn2 = gate2.getSPN();
        
        for (Species species : spn2.getSpeciess().values()) {
            result.addSpecies(species);
        }        
        for (Entry<String, Integer> marking : spn2.getInitialMarkings().entrySet()) {
            result.setInitialMarking(marking.getKey(), marking.getValue());
        }        
        for (Reaction reaction : spn2.getReactions().values()) {
            result.addReaction(reaction);
        }
        
        ArrayList<String> tempInputProteins = new ArrayList<String>(gate1.inputProteins);
        tempInputProteins.addAll(gate2.inputProteins);
        
        for (String inputProtein : tempInputProteins) {
            if (inputProtein.equals(gate1.outputProtein)) {
                newGate.intermediateProteins.add(inputProtein);
                newGate.outputProtein = gate2.outputProtein;
            } else if (inputProtein.equals(gate2.outputProtein)) {
                newGate.intermediateProteins.add(inputProtein);
                newGate.outputProtein = gate1.outputProtein;
            } else {
                newGate.inputProteins.add(inputProtein);
            }
        }
        
        newGate.setSPN(result);
        
        return newGate;
    }

    public int id;
    public String sbmlFile;
    public int cost;
    
    public ArrayList<String> inputProteins;
    public ArrayList<String> intermediateProteins;
    public String outputProtein;
    
    public String SOP;
    
    public int stableStateTime;
    
    private StochasticPetriNet spn = null;
    
    public SBGate(int id, int cost) {
        this.id = id;
        this.cost = cost;
        this.inputProteins = new ArrayList<String>();
        this.intermediateProteins = new ArrayList<String>();
        this.outputProtein = "";
    }
    
    public SBGate(int id, String sbmlFile, int cost,
            ArrayList<String> input, ArrayList<String> intm, String output, String SOP,
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
        if (spn == null) {
            if (sbmlFile == null) {
                throw new RuntimeException("No file specified");
            }            
            try {
                SBMLParser parser = new SBMLParser();
                
                ArrayList<String> proteins = new ArrayList<String>(inputProteins);
                proteins.add(outputProtein);
                parser.setPrependId(""+id, proteins);
                
                parser.readFile(sbmlFile);
                spn = parser.parse();
            } catch (IOException e) {
                Util.log.error("", e);
            }
        }
        return spn;
    }
    
    public void setSPN(StochasticPetriNet spn) {
        this.spn = spn;
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
