package dk.dtu.ls.library;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import dk.dtu.sb.Util;
import dk.dtu.sb.parser.SBMLParser;
import dk.dtu.sb.spn.Reaction;
import dk.dtu.sb.spn.Species;
import dk.dtu.sb.spn.StochasticPetriNet;
import dk.dtu.techmap.AIG;

public class SBGate implements Comparable<SBGate> {

    public static final int LOW = 0;
    public static final int HIGH = 100;

    public int id;
    public String sbmlFile;
    public int repressors = 0;
    public int activators = 0;

    public HashSet<String> inputProteins = new HashSet<String>();
    public HashSet<String> intermediateProteins = new HashSet<String>();
    public String outputProtein = null;
    public HashSet<String> outputProteins = new HashSet<String>();

    public String SOP;
    public int stableStateTime;
    
    public boolean isLibraryPart = true;
    public Set<SBGate> composedOf = null;
    
    private StochasticPetriNet spn = null;
    private AIG aig = null;

    public SBGate(int id) {
        this.id = id;
    }

    public SBGate(int id, String SOP) {
        this.id = id;
        this.SOP = SOP;
        this.outputProtein = getAIG().getOutputProtein();
    }

    public SBGate(int id, int repressors, int activators) {
        this.id = id;
        this.repressors = repressors;
        this.activators = activators;
    }

    public SBGate(int id, String sbmlFile, int repressors, int activators,
            HashSet<String> input, HashSet<String> intm, String output,
            String SOP, int stableTime) {
        this.id = id;
        this.sbmlFile = "library/sbml/" + sbmlFile;
        this.repressors = repressors;
        this.activators = activators;
        this.inputProteins = input;
        this.intermediateProteins = intm;
        this.outputProtein = output;
        this.SOP = SOP;
        this.stableStateTime = stableTime;
    }

    public StochasticPetriNet getSPN() {
        if (spn == null) {
            if (sbmlFile == null) {
                throw new RuntimeException("No file specified");
            }
            try {
                SBMLParser parser = new SBMLParser();

                ArrayList<String> proteins = new ArrayList<String>(
                        inputProteins);
                proteins.add(outputProtein);
                proteins.addAll(intermediateProteins);
                parser.setPrependId("" + id, proteins);

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

    public AIG getAIG() {
        if (aig == null) {
            aig = new AIG(SOP);
        }
        return aig;
    }

    public int getCost() {
        int cost = 0;
        if (repressors > 0) {
            cost += Math.pow(2, repressors);
        }
        if (activators > 0) {
            cost += Math.pow(2, activators);
        }
        return cost;
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
        if (getCost() > o.getCost()) {
            return 1;
        } else if (getCost() < o.getCost()) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "part_" + id;
    }

    public static SBGate compose(SBGate gate1, SBGate gate2) {
        SBGate newGate = new SBGate(gate1.hashCode() + gate2.hashCode(),
                gate1.repressors + gate2.repressors, gate1.activators
                        + gate2.activators);

        // compose SPN
        StochasticPetriNet result = gate1.getSPN().clone();
        StochasticPetriNet spn2 = gate2.getSPN().clone();
        for (Species species : spn2.getSpeciess().values()) {
            result.addSpecies(species);
        }
        for (Entry<String, Integer> marking : spn2.getInitialMarkings()
                .entrySet()) {
            result.setInitialMarking(marking.getKey(), marking.getValue());
        }
        for (Reaction reaction : spn2.getReactions().values()) {
            result.addReaction(reaction);
        }
        newGate.setSPN(result);

        // set intermediate
        newGate.intermediateProteins.addAll(gate1.intermediateProteins);
        newGate.intermediateProteins.addAll(gate2.intermediateProteins);

        // set new output
        if (gate1.outputProtein != null) {
            newGate.outputProteins.add(gate1.outputProtein);
        }
        if (gate2.outputProtein != null) {
            newGate.outputProteins.add(gate2.outputProtein);
        }
        newGate.outputProteins.addAll(gate1.outputProteins);
        newGate.outputProteins.addAll(gate2.outputProteins);

        // connect input and output, and set intermediate
        connectIO(gate1.inputProteins, newGate);
        connectIO(gate2.inputProteins, newGate);

        return newGate;
    }

    private static void connectIO(Set<String> inputProteins, SBGate newGate) {
        for (String inputProtein : inputProteins) {
            if (newGate.outputProteins.contains(inputProtein)) {
                newGate.intermediateProteins.add(inputProtein);
                newGate.outputProteins.remove(inputProtein);
            } else if (!newGate.intermediateProteins.contains(inputProtein)) {
                newGate.inputProteins.add(inputProtein);
            }
        }
    }

    public static SBGate compose(Set<SBGate> gatesSet) {
        ArrayList<SBGate> gates = new ArrayList<SBGate>(gatesSet);
        if (gates.size() > 1) {
            SBGate newGate = SBGate.compose(gates.get(0), gates.get(1));
            for (int i = 2; i < gates.size(); i++) {
                newGate = SBGate.compose(newGate, gates.get(i));
            }
            if (newGate.outputProteins.size() > 1) {
                throw new RuntimeException(
                        "There was more than one output protein...");
            }
            newGate.outputProtein = newGate.outputProteins.iterator().next();
            newGate.composedOf = gatesSet;
            newGate.isLibraryPart = false;
            return newGate;
        } else {
            return gates.get(0);
        }
    }
    
    public static SBGate loadFromProperties(String filename) {
        SBGate gate = null;
        Properties props = new Properties();
        
        try {
            props.load(new FileInputStream(filename));
            gate = new SBGate(Integer.parseInt(props.getProperty(Library.KEY_ID)));
            gate.sbmlFile = "library/sbml/" + props.getProperty(Library.KEY_SBML_FILE);
            gate.repressors = Integer.parseInt(props.getProperty(Library.KEY_REPRESSORS));
            gate.activators = Integer.parseInt(props.getProperty(Library.KEY_ACTIVATORS));
            String input = props.getProperty(Library.KEY_INPUT).trim();
            if (input.equals("")) {
                gate.inputProteins = new HashSet<String>();
            } else {
                gate.inputProteins = new HashSet<String>(Arrays.asList(input.split(",")));
            }
            String intermediate = props.getProperty(Library.KEY_INTERMEDIATE).trim();
            if (intermediate.equals("")) {
                gate.intermediateProteins = new HashSet<String>();
            } else {
                gate.intermediateProteins = new HashSet<String>(Arrays.asList(intermediate.split(",")));
            }
            gate.outputProtein = props.getProperty(Library.KEY_OUTPUT);
            gate.SOP = props.getProperty(Library.KEY_SOP);;
            gate.stableStateTime = Integer.parseInt(props.getProperty(Library.KEY_STEADY));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Util.log.error("", e);
        }   
        
        return gate;
    }
}
