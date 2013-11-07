package dk.dtu.sb.data;

import java.util.Collections;
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
    private double rate;

    /**
     * Map of threadID's and calculated propensity. Significantly speeds up the implementation.
     */
    private Map<Long, Double> propensity = new HashMap<Long, Double>();

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
     * @param rate
     *            See {@link #rate}.
     */
    public Reaction(String id, double rate) {
        this(id, id, rate);
    }

    /**
     * Constructs a reaction.
     * 
     * @param id
     *            See {@link #id}.
     * @param name
     *            See {@link #name}.
     * @param rate
     *            See {@link #rate}.
     */
    public Reaction(String id, String name, double rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
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
        return name != null ? name : id;
    }

    /**
     * See {@link #rate}.
     */
    public double getRate() {
        return rate;
    }

    /**
     * See {@link #propensity}.
     */
    public void setPropensity(double propensity, Long tid) {
    	synchronized(this){
    		this.propensity.put(tid, propensity);
    	}
        
    }

    /**
     * See {@link #propensity}.
     */
    public double getPropensity(Long tid) {
        return propensity.get(tid);
    }

    public String toString() {
        String s = "id : " + name + ", rate : " + rate + "\n";
        s += "  Reactants :" + reactants + "\n";
        s += "  Products  :" + products + "\n";
        return s;
    }
}
