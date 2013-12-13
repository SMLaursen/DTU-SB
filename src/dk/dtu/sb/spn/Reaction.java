package dk.dtu.sb.spn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a transition in the {@link StochasticPetriNet}.
 */
public class Reaction {

    private String id;
    private String name;
    private RateFunction rateFunction;
    private Map<String, Integer> reactants = new HashMap<String, Integer>();
    private Map<String, Integer> products = new HashMap<String, Integer>();
    private List<String> modifiers = new ArrayList<String>();

    /**
     * Constructs a reaction.
     * 
     * @param id
     *            See {@link #id}.
     * @param rateFunction
     *            See {@link #rateFunction}.
     */
    public Reaction(String id, RateFunction rateFunction) {
        this(id, id, rateFunction);
    }

    /**
     * Constructs a reaction.
     * 
     * @param id
     *            See {@link #id}.
     * @param name
     *            See {@link #name}.
     * @param rateFunction
     *            See {@link #rateFunction}.
     */
    public Reaction(String id, String name, RateFunction rateFunction) {
        this.id = id;
        this.name = name;
        this.rateFunction = rateFunction;
    }

    /**
     * Add reactant to this reaction with multiplicity 1.
     * 
     * @param speciesId
     *            An identifier for an instance of the Species-class found in
     *            the associated {@link StochasticPetriNet}.
     */
    public void addReactant(String speciesId) {
        addReactant(speciesId, 1);
    }

    /**
     * Add reactant to this reaction.
     * 
     * @param speciesId
     *            An identifier for an instance of the Species-class found in
     *            the associated {@link StochasticPetriNet}.
     * @param multiplicity
     *            The amount subtracted from this reactant when this reaction is
     *            fired.
     */
    public void addReactant(String speciesId, int multiplicity) {
        reactants.put(speciesId, multiplicity);
    }

    /**
     * Remove the reactant from the reactants of this reaction.
     * 
     * @param speciesId
     *            An identifier for an instance of the Species-class found in
     *            the associated {@link StochasticPetriNet}
     */
    public void removeReactant(String speciesId) {
        reactants.remove(speciesId);
    }

    /**
     * Get all reactants of this reaction.
     */
    public Map<String, Integer> getReactants() {
        return reactants;
    }

    /**
     * Add product to this reaction with multiplicity 1.
     * 
     * @param speciesId
     *            An identifier for an instance of the Species-class found in
     *            the associated {@link StochasticPetriNet}.
     */
    public void addProduct(String speciesId) {
        addProduct(speciesId, 1);
    }

    /**
     * Add product to this reaction.
     * 
     * @param speciesId
     *            An identifier for an instance of the Species-class found in
     *            the associated {@link StochasticPetriNet}.
     * @param multiplicity
     *            The amount added to this product when this reaction is fired.
     */
    public void addProduct(String speciesId, int multiplicity) {
        products.put(speciesId, multiplicity);
    }

    /**
     * Remove product from the products of this reaction.
     * 
     * @param speciesId
     *            An identifier for an instance of the Species-class found in
     *            the associated {@link StochasticPetriNet}.
     */
    public void removeProduct(String speciesId) {
        products.remove(speciesId);
    }

    /**
     * Get all products of this reaction.
     */
    public Map<String, Integer> getProducts() {
        return products;
    }

    /**
     * Adds a modifier for this reaction. 
     * 
     * @param speciesId
     *            An identifier for an instance of the Species-class found in
     *            the associated {@link StochasticPetriNet}.
     */
    public void addModifier(String speciesId) {
        modifiers.add(speciesId);
    }

    /**
     * Remove the modifier
     * 
     * @param speciesId
     *            An identifier for an instance of the Species-class found in
     *            the associated {@link StochasticPetriNet}.
     */
    public void removeModifier(String speciesId) {
        modifiers.remove(speciesId);
    }

    /**
     * Get all modifiers of this reaction.
     */
    public List<String> getModifiers() {
        return modifiers;
    }

    /**
     * A human-readable label for this reaction.
     */
    public String getName() {
        return name;
    }

    /**
     * The unique id of this reaction.
     */
    public String getId() {
        return id;
    }

    /**
     * Can be used to represent the reaction in e.g. graphs.
     * 
     * @return {@link #getName()} if present, else {@link #getId()}.
     */
    public String getLabel() {
        return name != null && !name.trim().isEmpty() ? name : id;
    }

    /**
     * Calculates the rate based on the current markings and the rate function.
     * 
     * @param vars
     *            A map of variables and their current values.
     * @return The rate of this reaction.
     */
    public double getRate(Map<String, Integer> vars) {
        return rateFunction.getRate(vars);
    }

    /**
     * Getter method.
     * 
     * @return {@link RateFunction}
     */
    public RateFunction getRateFunction() {
        return rateFunction;
    }

    /**
     * Helper, decides whether this reaction can be fired with the given
     * marking.
     * 
     * @param reaction
     *            See {@link Reaction}.
     * @return true if the markings enables this reaction, else false.
     */
    public boolean canReact(Map<String, Integer> markings) {
        int multiplicity, oldMarking;
        for (Entry<String, Integer> reactantEntry : getReactants().entrySet()) {
            multiplicity = reactantEntry.getValue();
            oldMarking = markings.get(reactantEntry.getKey());
            if (oldMarking < multiplicity) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        String s = "id: " + id + ", rate: " + rateFunction + "\n";
        s += "  Reactants: " + reactants + "\n";
        s += "  Products:  " + products + "\n";
        return s;
    }

    public boolean equals(Object other) {
        return other instanceof Reaction && this.id == ((Reaction) other).id;
    }
}
