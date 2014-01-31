package dk.dtu.ls.library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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

    public static SBGate compose(SBGate gate1, SBGate gate2) {
        SBGate newGate = new SBGate(gate1.hashCode() + gate2.hashCode(),
                gate1.repressors + gate2.repressors, gate1.activators
                        + gate2.activators);

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

        // connect input and output, and set intermediate
        composeIntermediate(gate1.inputProteins, gate2.outputProtein, newGate);
        composeIntermediate(gate2.inputProteins, gate1.outputProtein, newGate);

        // find new output
        if (newGate.intermediateProteins.contains(gate1.outputProtein)) {
            newGate.outputProtein = gate2.outputProtein;
        } else {
            newGate.outputProtein = gate1.outputProtein;
        }

        return newGate;
    }

    private static void composeIntermediate(List<String> inputProteins,
            String outputProtein, SBGate newGate) {
        for (String inputProtein : inputProteins) {
            if (inputProtein.equals(outputProtein)) {
                newGate.intermediateProteins.add(inputProtein);
            } else {
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
            return newGate;
        } else {
            return gates.get(0);
        }
    }

    public int id;
    public String sbmlFile;
    public int repressors = 0;
    public int activators = 0;

    public ArrayList<String> inputProteins = new ArrayList<String>();
    public ArrayList<String> intermediateProteins = new ArrayList<String>();
    public String outputProtein = new String();

    public String SOP;

    public int stableStateTime;

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
            ArrayList<String> input, ArrayList<String> intm, String output,
            String SOP, int stableTime) {
        this.id = id;
        this.sbmlFile = "library/" + sbmlFile;
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
            cost += Math.pow(2, 1 / repressors);
        }
        if (activators > 0) {
            cost += Math.pow(2, 1 / activators);
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

    public String toString() {
        return "part_" + id;
    }
}
