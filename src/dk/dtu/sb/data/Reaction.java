package dk.dtu.sb.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a transition in the {@link StochasticPetriNet}.
 */
public class Reaction {

    /**
     * The unique id of this reaction.
     */
    private String id;

    /**
     * A human-readable label for this reaction.
     */
    private String name;
    
    /**
     * 
     */
    private RateFunction rateFunction;

    /**
     * The reactants in this reactions.
     */
    private Map<String, Integer> reactants = new HashMap<String, Integer>();

    /**
     * The products in this reaction.
     */
    private Map<String, Integer> products = new HashMap<String, Integer>();
    
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
     * Add reactant to this reaction.
     * 
     * @param speciesId
     */
    public void addReactant(String speciesId) {
        addReactant(speciesId, 1);
    }
    
    public void addReactant(String speciesId, int multiplicity) {
        reactants.put(speciesId, multiplicity);
    }

    /**
     * Remove the reactant from the reactants of this reaction.
     * 
     * @param reactantId
     */
    public void removeReactant(String reactantId) {
        reactants.remove(reactantId);
    }

    /**
     * Get all reactants of this reaction.
     * 
     * @return
     */
    public Map<String, Integer> getReactants() {
        return reactants;
    }

    /**
     * Add product to this reaction.
     * 
     * @param product
     *            See {@link Species}.
     */
    public void addProduct(String speciesId) {
        addProduct(speciesId, 1);
    }
    
    public void addProduct(String speciesId, int multiplicity) {
        products.put(speciesId, multiplicity);
    }

    /**
     * Remove product from the products of this reaction.
     * 
     * @param productId
     *            The unique id of the product.
     * @return The removed {@link Species}.
     */
    public void removeProduct(String speciesId) {
        products.remove(speciesId);
    }

    /**
     * Get all products of this reaction.
     * 
     * @return
     */
    public Map<String, Integer> getProducts() {
        return products;
    }

    /**
     * See {@link #name}.
     */
    public String getName() {
        return name;
    }

    /**
     * See {@link #id}.
     */
    public String getId() {
        return id;
    }

    /**
     * Can be used to represent the reaction in e.g. graphs.
     * 
     * @return {@link #name} if present, else {@link #id}.
     */
    public String getLabel() {
        return name != null && !name.trim().isEmpty() ? name : id;
    }

    /**
     * Calculates the rate based on the current markings and the rate function.
     * 
     * @param vars A map of variables and their current values.
     * @return The rate of this reaction.
     */
    public double getRate(Map<String, Integer> vars) {
        return rateFunction.getRate(vars);
    }
    
    /**
     * Getter method.
     * @return {@link RateFunction}
     */
    public RateFunction getRateFunction() {
        return rateFunction;
    }
    
    /**
     * 
     * @param reaction
     * @return
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
    
    public boolean equals(Reaction other) {
        return this.id == other.getId();
    }
}
