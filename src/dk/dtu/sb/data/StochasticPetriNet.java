package dk.dtu.sb.data;

import java.io.File;
import java.util.HashMap;
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
     * @param reaction
     */
    public void addReaction(Reaction reaction) {
        if (reactions.containsKey(reaction.getId())) {
            throw new RuntimeException("Reaction with ID " + reaction.getId() + " already defined.");
        }
                
        reactions.put(reaction.getId(), reaction);
    }

    /**
     * Removes and returns the reaction with the given name
     * 
     * @param reactionId
     * @return
     */
    public Reaction removeReaction(String reactionId) {
        return reactions.remove(reactionId);
    }

    /**
     * 
     * @param reactionId
     * @return
     */
    public Reaction getReaction(String reactionId) {
        return reactions.get(reactionId);
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
    public void setInitialMarking(String speciesId, Integer value) {
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
        String output = "Reactions :\n";
        for (Reaction reaction : reactions.values()) {
            output += reaction.toString() + "\n";
        }
        output += "\n initial markings :" + initialMarkings;
        return output;
    }
}
