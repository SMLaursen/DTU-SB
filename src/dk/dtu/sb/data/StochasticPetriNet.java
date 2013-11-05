package dk.dtu.sb.data;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 
 */
public class StochasticPetriNet {

    private Map<String, Reaction> reactions;
    private Map<String, Integer> initialMarkings;

    /**
	 * 
	 */
    public StochasticPetriNet() {
        reactions = new HashMap<String, Reaction>();
        initialMarkings = new HashMap<String, Integer>();
    }

    /**
     * Adds a reaction to the SPN.
     * 
     * @param r
     */
    public void addReaction(Reaction r) {
        if (reactions.containsKey(r.getName())) {
            throw new RuntimeException("Reaction " + r + " already defined.");
        }
        reactions.put(r.getName(), r);
    }

    /**
     * Removes and returns the reaction with the given name
     * 
     * @param r
     * @return
     */
    public Reaction removeReaction(String r) {
        return reactions.remove(r);
    }

    /**
     * 
     * @param r
     * @return
     */
    public Reaction getReaction(String r) {
        return reactions.get(r);
    }

    /**
     * 
     * @return
     */
    public Map<String, Reaction> getReactions() {
        return reactions;
    }

    /**
     * Sets the initial markings. Replaces old values.
     * 
     * @param reactant
     * @param value
     */
    public void setInitialMarkings(String reactant, Integer value) {
        initialMarkings.put(reactant, value);
    }

    /**
     * 
     * @param reactant
     * @return
     */
    public int getInitialMarkings(String reactant) {
        return initialMarkings.get(reactant);
    }

    /**
     * 
     * @return
     */
    public Map<String, Integer> getInitialMarkings() {
        return initialMarkings;
    }

    /**
     * Returns a file with the visual dot representation
     * 
     * @return Graph in .dot format
     */
    public String toGraphviz() {
        // Use http://sandbox.kidstrythisathome.com/erdos/
        HashSet<String> transitions = new HashSet<String>();
        String graph = "digraph G {\n";

        for (Reaction reaction : reactions.values()) {

            // Process the reactants
            for (String reactant : reaction.getReactants().keySet()) {

                // Add transitions only once
                if (!transitions.contains(reaction.getName())) {
                    graph += "\"" + reaction.getName() + " ["
                            + reaction.getRate() + "]\"" + " [shape=box];\n";
                    transitions.add(reaction.getName());
                }

                graph += reactant + " -> " + "\"" + reaction.getName() + " ["
                        + reaction.getRate() + "]\"";
                // Set multiplicity on edges
                if (reaction.getReactants().get(reactant) > 1) {
                    graph += " [label = "
                            + reaction.getReactants().get(reactant) + "]";
                }
                graph += ";\n";
            }

            // Process the products
            for (String product : reaction.getProducts().keySet()) {
                graph += "\"" + reaction.getName() + " [" + reaction.getRate()
                        + "]\"" + " -> " + product;
                // Set multiplicity on edges
                if (reaction.getProducts().get(product) > 1) {
                    graph += " [label = " + reaction.getProducts().get(product)
                            + "]";
                }
                graph += ";\n";
            }
        }
        graph += "}";

        return graph;
    }

    /**
     * Returns a file with the PNML encoding.
     * 
     * @return
     */
    public File toPNML() {
        // TODO
        return null;
    }

    public String toString() {
        String s = "Reactions :\n";
        for (Reaction r : reactions.values()) {
            s += r.toString() + "\n";
        }
        s += "\n initial markings :";
        s += initialMarkings;
        return s;
    }
}
