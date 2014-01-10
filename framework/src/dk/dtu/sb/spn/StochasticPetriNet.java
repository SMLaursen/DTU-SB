package dk.dtu.sb.spn;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
     * 
     */
    private Map<String, Species> species = new HashMap<String, Species>();

    /**
     * Adds a reaction to the SPN.
     * 
     * @param reaction
     */
    public void addReaction(Reaction reaction) {
        if (reactions.containsKey(reaction.getId())) {
            throw new RuntimeException("Reaction with ID " + reaction.getId()
                    + " already defined.");
        }

        for (String speciesId : reaction.getProducts().keySet()) {
            if (!species.containsKey(speciesId)) {
                throw new RuntimeException("The species with the ID "
                        + speciesId + " was not found in this SPN.");
            } else {
                species.get(speciesId).addAsProductReaction(reaction);
            }
        }

        for (String speciesId : reaction.getReactants().keySet()) {
            if (!species.containsKey(speciesId)) {
                throw new RuntimeException("The species with the ID "
                        + speciesId + " was not found in this SPN.");
            } else {
                species.get(speciesId).addAsReactantReaction(reaction);
            }
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

    public void addSpecies(Species species) {
        this.species.put(species.getId(), species);
    }

    public Species getSpecies(String speciesId) {
        return species.get(speciesId);
    }

    public Map<String, Species> getSpeciess() {
        return species;
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
    public int getInitialMarking(String speciesId) {
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
        String graph = "digraph G {\nsize=\"4,4\";\nfontsize=\"10\";nodesep=\"2.0\";\n";

        for (Reaction reaction : reactions.values()) {

            graph += "\"" + reaction.getLabel() + " ["
                    + reaction.getRateFunction() + "]\"" + " [shape=box];\n";

            // Process the reactants
            for (Entry<String, Integer> reactantEntry : reaction.getReactants()
                    .entrySet()) {
                graph += "\"" + getSpecies(reactantEntry.getKey()).getLabel()
                        + "[" + reactantEntry.getKey() + "]" + " ("
                        + getInitialMarking(reactantEntry.getKey()) + ")\" -> "
                        + "\"" + reaction.getLabel() + " ["
                        + reaction.getRateFunction() + "]\"";
                // Set multiplicity on edges
                if (reactantEntry.getValue() > 1) {
                    graph += " [label = \"" + reactantEntry.getValue() + "\"]";
                }
                graph += ";\n";
            }

            // Process the products
            for (Entry<String, Integer> productEntry : reaction.getProducts()
                    .entrySet()) {
                graph += "\"" + reaction.getLabel() + " ["
                        + reaction.getRateFunction() + "]\"" + " -> \""
                        + getSpecies(productEntry.getKey()).getLabel() + "["
                        + productEntry.getKey() + "]" + " ("
                        + getInitialMarking(productEntry.getKey()) + ")\"";
                // Set multiplicity on edges
                if (productEntry.getValue() > 1) {
                    graph += " [label = \"" + productEntry.getValue() + "\"]";
                }
                graph += ";\n";
            }

            // Process the modifiers
            for (String modifier : reaction.getModifiers()) {
                graph += "\"" + getSpecies(modifier).getLabel() + "["
                        + modifier + "]" + " (" + getInitialMarking(modifier)
                        + ")\" -> \"" + reaction.getLabel() + " ["
                        + reaction.getRateFunction() + "]\" [style=dotted];\n";
            }
        }
        graph += "}";

        return graph;
    }

    @Override
    public StochasticPetriNet clone() {
        StochasticPetriNet s = new StochasticPetriNet();
        s.initialMarkings.putAll(initialMarkings);
        s.reactions.putAll(reactions);
        return s;
    }

    @Override
    public String toString() {
        String output = "Reactions:\n";
        for (Reaction reaction : reactions.values()) {
            output += " " + reaction;
        }
        output += "Initial markings:\n " + initialMarkings;
        return output;
    }
}