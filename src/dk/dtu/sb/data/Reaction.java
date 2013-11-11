package dk.dtu.sb.data;

import java.util.HashMap;
import java.util.Map;

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
    private Map<String, Species> reactants = new HashMap<String, Species>();

    /**
     * The products in this reaction.
     */
    private Map<String, Species> products = new HashMap<String, Species>();

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
     * @param reactant
     *            See {@link Species}.
     */
    public void addReactant(Species reactant) {
        reactants.put(reactant.getId(), reactant);
    }

    /**
     * Remove the reactant from the reactants of this reaction.
     * 
     * @param reactantId
     *            The unique id of the reactant.
     * @return The removed {@link Species}.
     */
    public Species removeReactant(String reactantId) {
        return reactants.remove(reactantId);
    }

    /**
     * Get all reactants of this reaction.
     * 
     * @return
     */
    public Map<String, Species> getReactants() {
        return reactants;
    }

    /**
     * Add product to this reaction.
     * 
     * @param product
     *            See {@link Species}.
     */
    public void addProduct(Species product) {
        products.put(product.getId(), product);
    }

    /**
     * Remove product from the products of this reaction.
     * 
     * @param productId
     *            The unique id of the product.
     * @return The removed {@link Species}.
     */
    public Species removeProduct(String productId) {
        return products.remove(productId);
    }

    /**
     * Get all products of this reaction.
     * 
     * @return
     */
    public Map<String, Species> getProducts() {
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

    public String toString() {
        String s = "id: " + id + ", rate: " + rateFunction + "\n";
        s += "  Reactants: " + reactants + "\n";
        s += "  Products:  " + products + "\n";
        return s;
    }
}
