package dk.dtu.sb.data;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 
 */
public class StochasticPetriNet {

    /**
     * 
     */
    private Map<String, Reaction> reactions = new HashMap<String, Reaction>();
        
    /**
     * 
     */
    private Map<String, Integer> initialMarkings = new HashMap<String, Integer>();

    /**
     * Adds a reaction to the SPN.
     * 
     * @param r
     */
    public void addReaction(Reaction r) {
        if (reactions.containsKey(r.getId())) {
            throw new RuntimeException("Reaction " + r + " already defined.");
        }
                
        reactions.put(r.getId(), r);
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
     * @param speciesId
     * @param value
     */
    public void setInitialMarkings(String speciesId, Integer value) {
        initialMarkings.put(speciesId, value);
    }

    /**
     * 
     * @param speciesId
     * @return
     */
    public int getInitialMarkings(String speciesId) {
        return initialMarkings.get(speciesId);
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
        String graph = "digraph G {\n";

        for (Reaction reaction : reactions.values()) {

            graph += "\"" + reaction.getLabel() + " [" + reaction.getRate()
                    + "]\"" + " [shape=box];\n";

            // Process the reactants
            for (Species reactant : reaction.getReactants().values()) {
                graph += "\"" + reactant.getLabel() + "\" -> " + "\""
                        + reaction.getLabel() + " [" + reaction.getRate()
                        + "]\"";
                // Set multiplicity on edges
                if (reactant.getMultiplicity() > 1) {
                    graph += " [label = \"" + reactant.getMultiplicity() + "\"]";
                }
                graph += ";\n";
            }

            // Process the products
            for (Species product : reaction.getProducts().values()) {
                graph += "\"" + reaction.getLabel() + " [" + reaction.getRate()
                        + "]\"" + " -> \"" + product.getLabel() +"\"";
                // Set multiplicity on edges
                if (product.getMultiplicity() > 1) {
                    graph += " [label = \"" + product.getMultiplicity() + "\"]";
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
